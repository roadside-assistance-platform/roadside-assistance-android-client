package esi.roadside.assistance.client.main.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SubmitResponseModel(
    val id: String,
    val clientId: String,
    val providerId: String?,
    val price: Int = 0,
    val serviceLocation: String
)
