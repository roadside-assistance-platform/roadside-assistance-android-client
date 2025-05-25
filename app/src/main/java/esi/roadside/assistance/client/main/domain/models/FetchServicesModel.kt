package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.main.data.dto.Statistics

data class FetchServicesModel(
    val status: String = "",
    val data: FetchServicesDataModel = FetchServicesDataModel()
)

data class FetchServicesDataModel(
    val services: List<ServiceModel> = emptyList(),
    val client: ClientInfo = ClientInfo(),
    val statistics: Statistics = Statistics()
)
