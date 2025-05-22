package esi.roadside.assistance.client.main.presentation.routes.home.request

import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.domain.models.LocationModel

data class RequestAssistanceState(
    val sheetVisible: Boolean = false,
    val category: Categories = Categories.TOWING,
    val loading: Boolean = false,
    val location: LocationModel? = null,
    val description: String = "",
    val isAIDetectionActive: Boolean = false,
    val isProcessingAI: Boolean = false
)
