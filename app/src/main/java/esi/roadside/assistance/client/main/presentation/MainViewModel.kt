package esi.roadside.assistance.client.main.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.core.util.account.AccountManager
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.ExitToAuthActivity
import esi.roadside.assistance.client.core.presentation.util.Event.MainNavigate
import esi.roadside.assistance.client.core.presentation.util.Event.ShowMainActivityMessage
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.main.domain.models.ClientInfo
import esi.roadside.assistance.client.main.domain.models.FetchServicesModel
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.repository.ServiceAction
import esi.roadside.assistance.client.main.domain.repository.ServiceManager
import esi.roadside.assistance.client.main.domain.use_cases.DirectionsUseCase
import esi.roadside.assistance.client.main.domain.use_cases.FetchServices
import esi.roadside.assistance.client.main.domain.use_cases.Logout
import esi.roadside.assistance.client.main.presentation.routes.home.HomeUiState
import esi.roadside.assistance.client.main.util.QueuesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.onSuccess

class MainViewModel(
    private val accountManager: AccountManager,
    val logoutUseCase: Logout,
    val serviceManager: ServiceManager,
    val directionsUseCaseUseCase: DirectionsUseCase,
    val queuesManager: QueuesManager,
    val fetchServices: FetchServices,
): ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _servicesHistory = MutableStateFlow<FetchServicesModel?>(null)
    val servicesHistory = _servicesHistory.asStateFlow()

    private val _client = MutableStateFlow(ClientInfo())
    val client = _client.asStateFlow()

    val currentService = serviceManager.service

    private val _notifications = MutableStateFlow(listOf<NotificationModel>())
    val notifications = _notifications.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.getUserFlow().collectLatest { client ->
                _client.value = client.toClientInfo()
                onAction(Action.FetchServices)
                launch(Dispatchers.IO) {
                    queuesManager.consumeUserNotifications(client.id, "client")
                }
                launch(Dispatchers.IO) {
                    queuesManager.serviceAcceptance.receiveAsFlow().collectLatest { service ->
                        serviceManager.onAction(ServiceAction.Accepted(service.provider))
                    }
                }
                launch(Dispatchers.IO) {
                    queuesManager.locationUpdate.receiveAsFlow().collectLatest { providerLocation ->
                        if (currentService.value.clientState == ClientState.PROVIDER_IN_WAY) {
                            serviceManager.onAction(
                                ServiceAction.LocationUpdate(
                                    LocationModel(
                                        providerLocation.longitude,
                                        providerLocation.latitude
                                    ),
                                    providerLocation.eta
                                )
                            )
                            currentService.value.serviceModel?.serviceLocation?.let { location ->
                                Log.i("MainViewModel", "Location: $location, Provider: $providerLocation")
                                directionsUseCaseUseCase(
                                    LocationModel(providerLocation.longitude, providerLocation.latitude)
                                    to
                                    location
                                ).onSuccess { result ->
                                    result.routes.minByOrNull { it.duration }?.let { route ->
                                        _homeUiState.update {
                                            it.copy(directions = route)
                                        }
                                        serviceManager.onAction(ServiceAction.SetDistance(route.distance))
                                    }
                                }.onError {
                                    sendEvent(ShowMainActivityMessage(it.text))
                                }
                            }
                        }
                    }
                }
                launch(Dispatchers.IO) {
                    queuesManager.providerArrival.receiveAsFlow().collectLatest {
                        if (currentService.value.clientState == ClientState.PROVIDER_IN_WAY) {
                            serviceManager.onAction(ServiceAction.Arrived)
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: Action) {
        when(action) {
            is Action.Navigate -> sendEvent(MainNavigate(action.route))
            Action.Logout -> {
                viewModelScope.launch(Dispatchers.IO) {
                    logoutUseCase()
                    sendEvent(ExitToAuthActivity)
                }
            }
            Action.CancelRequest -> {
                viewModelScope.launch(Dispatchers.IO) {
                    serviceManager.onAction(ServiceAction.Cancel)
                }
            }

            Action.WorkingFinished -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _homeUiState.update {
                        it.copy(loading = true)
                    }
                    serviceManager.onAction(ServiceAction.WorkFinished)
                    _homeUiState.update {
                        it.copy(loading = false, directions = null)
                    }
                }
            }

            is Action.CompleteRequest -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _homeUiState.update {
                        it.copy(loading = true)
                    }
                    serviceManager.onAction(ServiceAction.Complete(action.rating))
                    _homeUiState.update {
                        it.copy(loading = false, directions = null)
                    }
                }
            }
            Action.SendMessage -> {
                _homeUiState.update {
                    it.copy(message = "")
                }
                viewModelScope.launch(Dispatchers.IO) {
                    serviceManager.onAction(ServiceAction.SendMessage(_homeUiState.value.message))
                }
            }
            is Action.SetMessage -> {
                _homeUiState.update {
                    it.copy(message = action.message)
                }
            }
            is Action.SetLocation -> {
                _homeUiState.update {
                    it.copy(location = action.location)
                }
            }
            Action.FetchServices -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _homeUiState.update {
                        it.copy(servicesLoading = true)
                    }
                    fetchServices().onSuccess { service ->
                        _servicesHistory.value = service
                        _homeUiState.update {
                            it.copy(servicesLoading = false)
                        }
                    }.onError {
                        Log.e("MainViewModel", "Error fetching services: ${it.text}")
                        _homeUiState.update {
                            it.copy(servicesLoading = false)
                        }
                    }
                }
            }
        }
    }
}