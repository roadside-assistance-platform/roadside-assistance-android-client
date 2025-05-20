package esi.roadside.assistance.client.main.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CompletionResponse(
    val serviceId: String,
    val price: Int,
    val status: String
)
