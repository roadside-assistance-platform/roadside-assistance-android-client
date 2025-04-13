package esi.roadside.assistance.client.main.presentation.routes.home.request

import esi.roadside.assistance.client.main.domain.Categories

data class RequestAssistanceState(
    val sheetVisible: Boolean = false,
    val category: Categories = Categories.TOWING,
    val description: String = ""
)
