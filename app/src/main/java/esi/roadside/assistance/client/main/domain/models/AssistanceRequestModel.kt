package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.main.data.dto.AssistanceRequest
import esi.roadside.assistance.client.main.domain.Categories

data class AssistanceRequestModel(
    val description: String = "",
    val serviceCategory: Categories,
    val serviceLocation: LocationModel,
    val providerId: String = "",
    val price: Int,
) {
    fun toAssistanceRequest() = AssistanceRequest(
        description = description,
        serviceCategory = serviceCategory,
        serviceLocation = serviceLocation.toString(),
        providerId = providerId,
        price = price,
    )
}
