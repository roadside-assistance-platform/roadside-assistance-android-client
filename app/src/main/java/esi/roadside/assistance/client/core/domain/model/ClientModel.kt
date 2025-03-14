package esi.roadside.assistance.client.core.domain.model

import esi.roadside.assistance.client.core.data.dto.Client
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import java.time.ZonedDateTime

data class ClientModel(
    val id: String? = "",
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String,
    val photo: String,
    val services: List<ServiceModel> = emptyList(),
    val createdAt: ZonedDateTime ,
    val updatedAt: ZonedDateTime
) {
    fun toClient() = Client(
        id = id,
        fullName = fullName,
        email = email,
        password = password,
        phone = phone,
        photo = photo,
        services = services.map { it.toService() },
        createdAt = createdAt.toInstant().toEpochMilli()  ,
        updatedAt = updatedAt.toInstant().toEpochMilli()
    )
}
