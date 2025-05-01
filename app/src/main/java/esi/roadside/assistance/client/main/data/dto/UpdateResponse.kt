package esi.roadside.assistance.client.main.data.dto

import esi.roadside.assistance.client.core.data.dto.Service
import kotlinx.serialization.Serializable

@Serializable
data class UpdateResponse(
    val status: String,
    val data: UpdateData
)

@Serializable
data class UpdateData(val service: Service)