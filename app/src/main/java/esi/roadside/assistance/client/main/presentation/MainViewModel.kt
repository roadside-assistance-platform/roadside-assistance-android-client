package esi.roadside.assistance.client.main.presentation

import androidx.lifecycle.ViewModel
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.presentation.routes.home.HomeUiState
import esi.roadside.assistance.client.main.presentation.routes.home.request.RequestAssistanceState
import esi.roadside.assistance.client.main.presentation.routes.profile.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(

): ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

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
            Action.ConfirmProfileEditing -> TODO()
            Action.EnableProfileEditing -> {
                _profileUiState.update {
                    it.copy(enableEditing = true)
                }
            }
            is Action.Navigate -> sendEvent(Event.MainNavigate(action.route))
        }
    }
}