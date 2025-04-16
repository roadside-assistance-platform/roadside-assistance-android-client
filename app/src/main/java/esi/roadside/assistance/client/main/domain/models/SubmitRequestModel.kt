package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.main.domain.Categories
import kotlinx.serialization.Serializable

@Serializable
data class SubmitRequestModel(
    val description: String = "",
    val serviceCategory: Categories,
    val serviceLocation: String,
    val providerId: String = "",
    val price: Int,
)
