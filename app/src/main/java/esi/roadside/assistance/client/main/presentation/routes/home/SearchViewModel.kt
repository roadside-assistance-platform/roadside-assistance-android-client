package esi.roadside.assistance.client.main.presentation.routes.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.ShowMainActivityMessage
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.main.domain.use_cases.Geocoding
import esi.roadside.assistance.client.main.presentation.routes.home.search.SearchAction
import esi.roadside.assistance.client.main.presentation.routes.home.search.SearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val geocodingUseCase: Geocoding
): ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    fun onAction(action: SearchAction) {
        when(action) {
            SearchAction.Collapse -> _state.update {
                it.copy(expanded = false)
            }
            SearchAction.Expand -> _state.update {
                it.copy(expanded = true)
            }
            is SearchAction.UpdateExpanded -> _state.update {
                it.copy(expanded = action.expanded)
            }
            is SearchAction.UpdateQuery -> {
                _state.update {
                    it.copy(query = action.query)
                }
                if (action.query.isEmpty())
                    _state.update {
                        it.copy(result = null)
                    }
                else {
                    viewModelScope.launch {
                        geocodingUseCase(action.query).onSuccess { result ->
                            _state.update {
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
}