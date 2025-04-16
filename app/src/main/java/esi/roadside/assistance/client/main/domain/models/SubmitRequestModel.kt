package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.main.data.dto.SubmitRequest
import esi.roadside.assistance.client.main.domain.Categories
import kotlinx.serialization.Serializable

data class SubmitRequestModel(
    val description: String = "",
    val serviceCategory: Categories,
    val serviceLocation: LocationModel,
    val providerId: String = "",
    val price: Int,
) {
    fun toSubmitRequest() = SubmitRequest(
        description = description,
        serviceCategory = serviceCategory,
        serviceLocation = serviceLocation.toString(),
        providerId = providerId,
        price = price,
    )
}
