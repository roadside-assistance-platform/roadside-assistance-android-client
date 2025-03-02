package esi.roadside.assistance.client.auth.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun ViewModel.sendEvent(event: Event) {
    viewModelScope.launch {
        EventBus.sendEvent(event)
    }
}