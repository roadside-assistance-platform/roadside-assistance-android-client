package esi.roadside.assistance.client.main.presentation.routes.home.request

import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.models.LocationModel

sealed interface AssistanceAction {
    data object ShowSheet: AssistanceAction
    data object HideSheet: AssistanceAction
    data object Submit: AssistanceAction
    data class SelectCategory(val category: Categories): AssistanceAction
    data class SetDescription(val description: String): AssistanceAction
    data class SetLocation(val location: LocationModel?): AssistanceAction
}