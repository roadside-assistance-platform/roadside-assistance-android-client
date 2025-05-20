package esi.roadside.assistance.client.main.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class CompletionRequest(
    val serviceId: String? = null,
    val distance: Double = 0.0,
    val completionTime: String = "",
)
