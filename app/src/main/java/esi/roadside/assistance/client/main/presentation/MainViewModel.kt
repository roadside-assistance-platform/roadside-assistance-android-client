package esi.roadside.assistance.client.main.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.auth.UserPreferences
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.use_case.Update
import esi.roadside.assistance.client.auth.util.dataStore
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Event.*
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.presentation.models.ClientUi
import esi.roadside.assistance.client.main.presentation.routes.home.HomeUiState
import esi.roadside.assistance.client.main.presentation.routes.home.request.RequestAssistanceState
import esi.roadside.assistance.client.main.presentation.routes.profile.ProfileUiState
import esi.roadside.assistance.client.main.util.saveClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val context: Context,
    val updateUseCase: Update
): ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _client = MutableStateFlow(ClientUi())
    val client = _client.asStateFlow()

    private val _requestAssistanceState = MutableStateFlow(RequestAssistanceState())
    val requestAssistanceState = _requestAssistanceState.asStateFlow()

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val _notifications = MutableStateFlow(listOf<NotificationModel>())
    val notifications = _notifications.asStateFlow()

    init {
//        NotificationListener.listenForNotifications("") {
//            viewModelScope.launch {
//
//            }
//        }
        viewModelScope.launch {
            context.dataStore.data.collectLatest { userPreferences ->
                _profileUiState.update {
                    it.copy(
                        client = userPreferences.client.toClientModel().toClientUi(),
                        editClient = userPreferences.client.toClientModel().toClientUi()
                    )
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

            }
            Action.ConfirmProfileEditing -> {
                viewModelScope.launch {
                    updateUseCase(_profileUiState.value.editClient.toUpdateModel())
                        .onSuccess {
                            saveClient(context, it)
                            _profileUiState.update {
                                it.copy(enableEditing = false)
                            }
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
        }
    }
}