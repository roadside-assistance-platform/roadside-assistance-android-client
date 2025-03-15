package esi.roadside.assistance.client.core.domain.model

import esi.roadside.assistance.client.main.domain.models.ServiceModel
import java.time.ZonedDateTime

data class ClientModel(
    val id: String = "",
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String,
    val photo: String,
    val services: List<ServiceModel> = emptyList(),
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)