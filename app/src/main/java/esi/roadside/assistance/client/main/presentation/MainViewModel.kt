package esi.roadside.assistance.client.main.presentation

import android.content.Context
import android.os.CountDownTimer
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.domain.use_case.Cloudinary
import esi.roadside.assistance.client.auth.domain.use_case.Update
import esi.roadside.assistance.client.auth.util.dataStore
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.*
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.main.domain.PolymorphicNotification
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.ClientInfo
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.models.toLocationModel
import esi.roadside.assistance.client.main.domain.use_cases.DirectionsUseCase
import esi.roadside.assistance.client.main.domain.use_cases.FinishRequest
import esi.roadside.assistance.client.main.domain.use_cases.Geocoding
import esi.roadside.assistance.client.main.domain.use_cases.Logout
import esi.roadside.assistance.client.main.domain.use_cases.Rating
import esi.roadside.assistance.client.main.domain.use_cases.SubmitRequest
import esi.roadside.assistance.client.main.presentation.routes.home.HomeUiState
import esi.roadside.assistance.client.main.presentation.routes.home.SearchEvent
import esi.roadside.assistance.client.main.presentation.routes.home.SearchState
import esi.roadside.assistance.client.main.presentation.routes.home.request.RequestAssistanceState
import esi.roadside.assistance.client.main.presentation.routes.profile.ProfileUiState
import esi.roadside.assistance.client.main.util.QueuesManager
import esi.roadside.assistance.client.main.util.saveClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val context: Context,
    val cloudinary: Cloudinary,
    val updateUseCase: Update,
    val submitRequestUseCase: SubmitRequest,
    val finishRequestUseCase: FinishRequest,
    val ratingUseCase: Rating,
    val logoutUseCase: Logout,
    val geocodingUseCase: Geocoding,
    val directionsUseCaseUseCase: DirectionsUseCase,
    val queuesManager: QueuesManager,
): ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val _client = MutableStateFlow(ClientInfo())
    val client = _client.asStateFlow()

    private val _serviceAcceptance = MutableStateFlow<PolymorphicNotification.ServiceAcceptance?>(null)

    private var  _currentService: ServiceModel? = null

    private val _requestAssistanceState = MutableStateFlow(RequestAssistanceState())
    val requestAssistanceState = _requestAssistanceState.asStateFlow()

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val _notifications = MutableStateFlow(listOf<NotificationModel>())
    val notifications = _notifications.asStateFlow()

    private val timer = object: CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            _homeUiState.update {
                it.copy(time = millisUntilFinished)
            }
        }

        override fun onFinish() {
            onAction(Action.Timeout)
        }
    }

    init {
        //NotificationListener.listenForNotifications(_client.value.id)
        viewModelScope.launch(Dispatchers.IO) {
            context.dataStore.data.collectLatest { userPreferences ->
                _client.value = userPreferences.client.toClientInfo()
                _profileUiState.update {
                    it.copy(
                        client = userPreferences.client.toClientModel().toClientUi(),
                        editClient = userPreferences.client.toClientModel().toClientUi(),
                        photo = userPreferences.client.photo ?: ""
                    )
                }
                queuesManager.close()
                queuesManager.consumeUserNotifications(userPreferences.client.id, "client")
                launch(Dispatchers.IO) {
                    queuesManager.serviceAcceptance.consumeEach {
                        if (_homeUiState.value.clientState == ClientState.ASSISTANCE_REQUESTED) {
                            _serviceAcceptance.value = it
                            _homeUiState.update {
                                it.copy(clientState = ClientState.PROVIDER_IN_WAY)
                            }
                            timer.cancel()
                            _serviceAcceptance.value?.let { service ->
                                queuesManager.publishCategoryQueues(
                                    setOf(service.category),
                                    PolymorphicNotification.ServiceRemove(
                                        serviceId = service.id,
                                        exception = service.provider.id
                                    )
                                )
                            }
                        }
                    }
                }
                launch(Dispatchers.IO) {
                    queuesManager.locationUpdate.consumeEach { providerLocation ->
                        if (_homeUiState.value.clientState == ClientState.PROVIDER_IN_WAY) {
                            _homeUiState.update {
                                it.copy(providerLocation = Point.fromLngLat(providerLocation.longitude, providerLocation.latitude))
                            }
                            _homeUiState.value.location?.toLocationModel()?.let { location ->
                                directionsUseCaseUseCase(
                                    LocationModel(providerLocation.latitude, providerLocation.longitude)
                                    to
                                    location
                                ).onSuccess { result ->
                                    result.routes.minByOrNull { it.duration }?.let { route ->
                                        _homeUiState.update {
                                            it.copy(directions = route)
                                        }
                                    }
                                }.onError {
                                    sendEvent(ShowMainActivityMessage(it.text))
                                }
                            }
                        }
                    }
                }
                launch(Dispatchers.IO) {
                    queuesManager.providerArrival.consumeEach {
                        if (_homeUiState.value.clientState == ClientState.PROVIDER_IN_WAY) {
                            _homeUiState.update {
                                it.copy(clientState = ClientState.ASSISTANCE_IN_PROGRESS)
                            }
                        }
                    }
                }
            }
        }
    }

    fun onSearchEvent(event: SearchEvent) {
        when(event) {
            SearchEvent.Collapse -> _searchState.update {
                it.copy(expanded = false)
            }
            SearchEvent.Expand -> _searchState.update {
                it.copy(expanded = true)
            }
            is SearchEvent.UpdateExpanded -> _searchState.update {
                it.copy(expanded = event.expanded)
            }
            is SearchEvent.UpdateQuery -> {
                _searchState.update {
                    it.copy(query = event.query)
                }
                if (event.query.isEmpty())
                    _searchState.update {
                        it.copy(result = null)
                    }
                else {
                    viewModelScope.launch {
                        geocodingUseCase(event.query).onSuccess { result ->
                            _searchState.update {
                                it.copy(result = result)
                            }
                        }.onError {
                            sendEvent(ShowMainActivityMessage(it.text))
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: Action) {
        when(action) {
            is Action.SetLocation -> {
                _homeUiState.update {
                    it.copy(location = action.location)
                }
            }
            is Action.SelectCategory -> {
                _requestAssistanceState.update {
                    it.copy(category = action.category)
                }
            }
            is Action.SetDescription -> {
                _requestAssistanceState.update {
                    it.copy(description = action.description)
                }
            }
            Action.SubmitRequest -> {
                _requestAssistanceState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    _homeUiState.value.location?.let { location ->
                        submitRequestUseCase(
                            AssistanceRequestModel(
                                description = _requestAssistanceState.value.description,
                                serviceCategory = _requestAssistanceState.value.category,
                                serviceLocation = LocationModel.fromPoint(location),
                                price = 0
                            )
                        ).onSuccess {
                            sendEvent(ShowMainActivityMessage(R.string.request_submitted))
                            _homeUiState.update {
                                it.copy(clientState = ClientState.ASSISTANCE_REQUESTED)
                            }
                            _currentService = it
                            launch(Dispatchers.IO) {
                                queuesManager.publishCategoryQueues(
                                    setOf(_requestAssistanceState.value.category),
                                    PolymorphicNotification.Service(
                                        id = it.id,
                                        client = _client.value,
                                        description = _requestAssistanceState.value.description,
                                        serviceCategory = _requestAssistanceState.value.category,
                                        serviceLocation = LocationModel.fromPoint(location).toString(),
                                        provider = null,
                                        price = 0
                                    )
                                )
                            }
                            timer.cancel()
                            timer.start()
                        }.onError {
                            sendEvent(ShowMainActivityMessage(it.text))
                        }
                        _requestAssistanceState.update {
                            it.copy(sheetVisible = false, loading = false)
                        }
                    }
                }
            }
            Action.ConfirmProfileEditing -> {
                val inputError = ValidateInput.validateUpdateProfile(
                    _profileUiState.value.editClient.fullName,
                    _profileUiState.value.editClient.email,
                    _profileUiState.value.editClient.phone
                )
                if (inputError != null)
                    _profileUiState.update {
                        it.copy(
                            fullNameError = inputError.takeIf { it.field == Field.FULL_NAME },
                            emailError = inputError.takeIf { it.field == Field.EMAIL },
                            phoneError = inputError.takeIf { it.field == Field.PHONE_NUMBER }
                        )
                    }
                else {
                    _profileUiState.update { it.copy(loading = true) }
                    viewModelScope.launch {
                        cloudinary(
                            _profileUiState.value.editClient.photo ?: "".toUri(),
                            onSuccess = { url ->
                                _profileUiState.update {
                                    it.copy(
                                        photo = url,
                                    )
                                }
                            },
                            onFailure = {
                                sendEvent(ShowMainActivityMessage(R.string.error))
                            },
                            onFinished = {
                                viewModelScope.launch {
                                    updateUseCase(_profileUiState.value.editClient.toUpdateModel().copy(
                                        photo = _profileUiState.value.photo
                                    ))
                                        .onSuccess {
                                            saveClient(context, it)
                                        }
                                        .onError {
                                            sendEvent(ShowMainActivityMessage(it.text))
                                        }

                                    _profileUiState.update {
                                        it.copy(
                                            enableEditing = false,
                                            fullNameError = null,
                                            emailError = null,
                                            phoneError = null
                                        )
                                    }
                                    _profileUiState.update { it.copy(loading = false) }
                                }
                            },
                            onProgress = {}
                        )
                    }
                }
            }
            Action.EnableProfileEditing -> {
                _profileUiState.update {
                    it.copy(enableEditing = true)
                }
            }
            Action.CancelProfileEditing -> {
                _profileUiState.update {
                    it.copy(enableEditing = false, editClient = it.client)
                }
            }
            is Action.Navigate -> sendEvent(MainNavigate(action.route))
            is Action.EditClient -> {
                _profileUiState.update {
                    it.copy(editClient = action.client)
                }
            }
            Action.ShowRequestAssistance -> {
                _requestAssistanceState.update {
                    it.copy(sheetVisible = true)
                }
                sendEvent(ShowRequestAssistance)
            }
            Action.HideRequestAssistance -> {
                _requestAssistanceState.update {
                    it.copy(sheetVisible = false)
                }
                sendEvent(ShowRequestAssistance)
            }

            Action.Logout -> {
                viewModelScope.launch {
                    logoutUseCase()
                    sendEvent(ExitToAuthActivity)
                }
            }

            Action.CancelRequest -> {
                timer.cancel()
                _homeUiState.update {
                    it.copy(clientState = ClientState.IDLE)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    _currentService?.id?.let { id ->
                        queuesManager.publishCategoryQueues(
                            setOf(_requestAssistanceState.value.category),
                            PolymorphicNotification.ServiceRemove(
                                serviceId = id,
                                exception = null
                            )
                        )
                    }
                }
            }

            Action.Timeout -> {
                timer.cancel()
                _homeUiState.update {
                    it.copy(clientState = ClientState.ASSISTANCE_FAILED)
                }
            }

            Action.WorkingFinished -> {
                viewModelScope.launch {
                    _serviceAcceptance.value?.let { service ->
                        finishRequestUseCase(service.id)
                            .onSuccess { result ->
                                _homeUiState.update {
                                    it.copy(
                                        clientState = ClientState.ASSISTANCE_COMPLETED,
                                        directions = null,
                                        servicePrice = result.price
                                    )
                                }
                            }
                    }
                }
            }

            is Action.CompleteRequest -> {
                viewModelScope.launch {
                    _serviceAcceptance.value?.let { service ->
                        ratingUseCase(service.id, action.rating)
                            .onSuccess { result ->
                                _homeUiState.update {
                                    it.copy(clientState = ClientState.IDLE)
                                }
                                launch(Dispatchers.IO) {
                                    queuesManager.publishUserNotification(
                                        service.id,
                                        "provider",
                                        PolymorphicNotification.ServiceDone(
                                            price = _homeUiState.value.servicePrice,
                                            rating = action.rating
                                        )
                                    )
                                }
                            }.onError {
                                sendEvent(ShowMainActivityMessage(it.text))
                            }
                    }
                }
            }
        }
    }
}