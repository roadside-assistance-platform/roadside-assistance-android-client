package esi.roadside.assistance.client.main.data.dto

import esi.roadside.assistance.client.core.data.dto.Client
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClientResponse(
    val status: String,
    val message: String,
    val data: UpdateClientData
)

@Serializable
data class UpdateClientData(
    val user: Client
)
