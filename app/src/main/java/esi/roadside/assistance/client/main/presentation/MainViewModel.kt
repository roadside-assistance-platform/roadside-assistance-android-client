package esi.roadside.assistance.client.main.presentation

import androidx.lifecycle.ViewModel
import esi.roadside.assistance.client.main.presentation.routes.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    fun onAction(action: Action) {
        when(action) {
            Action.RequestService -> TODO()
            is Action.SetLocation -> {
                _homeUiState.update {
                    it.copy(location = it.location)
                }
            }
        }
    }
}