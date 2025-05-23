package esi.roadside.assistance.client.main.presentation.routes.home.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.core.presentation.util.Event.ShowRequestAssistance
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.repository.ServiceAction.Submit
import esi.roadside.assistance.client.main.domain.repository.ServiceManager
import esi.roadside.assistance.client.main.domain.services.VehicleIssueAIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AssistanceViewModel(
    private val serviceManager: ServiceManager,
    private val aiService: VehicleIssueAIService
): ViewModel() {
    private val _state = MutableStateFlow(RequestAssistanceState())
    val state = _state.asStateFlow()

    fun onAction(action: AssistanceAction) {
        when(action) {
            is AssistanceAction.SelectCategory -> {
                _state.update {
                    it.copy(category = action.category)
                }
            }
            is AssistanceAction.SetDescription -> {
                _state.update {
                    it.copy(description = action.description)
                }
            }
            is AssistanceAction.Submit -> {
                _state.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    serviceManager.onAction(
                        Submit(
                            AssistanceRequestModel(
                                description = _state.value.description,
                                serviceCategory = _state.value.category,
                                serviceLocation = _state.value.location ?:
                                    throw IllegalStateException("Location is null"),
                                price = 0
                            )
                        ) {
                        }
                    )
                    _state.update {
                        it.copy(sheetVisible = false, loading = false)
                    }
                }
            }
            AssistanceAction.ShowSheet -> {
                _state.update {
                    it.copy(sheetVisible = true, isAIDetectionActive = false, isProcessingAI = false,
                        description = "", category = Categories.TOWING)
                }
                sendEvent(ShowRequestAssistance)
            }
            AssistanceAction.HideSheet -> {
                _state.update {
                    it.copy(sheetVisible = false)
                }
                sendEvent(ShowRequestAssistance)
            }
            is AssistanceAction.SetLocation -> {
                _state.update {
                    it.copy(location = action.location)
                }
            }
            AssistanceAction.StartAIDetection -> {
                _state.update {
                    it.copy(isAIDetectionActive = true)
                }
            }
            AssistanceAction.CloseAIDetection -> {
                _state.update {
                    it.copy(isAIDetectionActive = false)
                }
            }
            is AssistanceAction.SubmitAIData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val category = aiService.detectCategoryFromImageUri(action.uri)
                    val description = aiService.generateDescriptionFromImageAndAudio(
                        action.uri, action.audio
                    )
                    _state.update {
                        it.copy(
                            category = category ?: Categories.OTHER,
                            description = description,
                            isProcessingAI = false,
                            isAIDetectionActive = false
                        )
                    }
                }
            }
        }
    }
}

