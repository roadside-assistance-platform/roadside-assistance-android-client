package esi.roadside.assistance.client.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.main.presentation.routes.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.rabbitmq.client.*
import esi.roadside.assistance.client.BuildConfig
import esi.roadside.assistance.client.main.domain.models.NotificationModel
import esi.roadside.assistance.client.main.util.NotificationListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    init {
        NotificationListener.listenForNotifications("") {
            viewModelScope.launch {

            }
        }
    }

    fun onAction(action: Action) {
        when(action) {
            Action.RequestService -> TODO()
            is Action.SetLocation -> {
                _homeUiState.update {
                    it.copy(location = action.location)
                }
            }
            Action.OpenNotifications -> TODO()
        }
    }
}