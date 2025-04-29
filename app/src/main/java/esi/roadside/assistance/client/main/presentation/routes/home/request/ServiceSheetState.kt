package esi.roadside.assistance.client.main.presentation.routes.home.request

import esi.roadside.assistance.client.main.domain.models.ServiceModel


data class ServiceSheetState(
    val service: ServiceModel = ServiceModel(),
    val loading: Boolean = false,
)
