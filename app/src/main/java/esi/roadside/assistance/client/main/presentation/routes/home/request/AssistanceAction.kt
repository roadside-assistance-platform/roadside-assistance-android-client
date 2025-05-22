package esi.roadside.assistance.client.main.presentation.routes.home.request

import android.net.Uri
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.models.LocationModel

sealed interface AssistanceAction {
    data object ShowSheet: AssistanceAction
    data object HideSheet: AssistanceAction
    data object Submit: AssistanceAction
    data object StartAIDetection: AssistanceAction
    data object CloseAIDetection: AssistanceAction
    data class SubmitAIData(val uri: Uri, val audio: String): AssistanceAction
    data class SelectCategory(val category: Categories): AssistanceAction
    data class SetDescription(val description: String): AssistanceAction
    data class SetLocation(val location: LocationModel?): AssistanceAction
}

