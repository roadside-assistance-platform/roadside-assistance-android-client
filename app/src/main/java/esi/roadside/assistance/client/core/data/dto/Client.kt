package esi.roadside.assistance.client.core.data.dto

import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.main.presentation.models.ClientUi
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.time.ZoneId

@Serializable
data class Client(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val photo: String = "",
    val services: List<Service> = emptyList(),
    val createdAt: String = "",
    val updatedAt: String = "",
) {
    fun toClientModel() = ClientModel(
        id = id,
        fullName = fullName,
        email = email,
        password = password,
        phone = phone,
        photo = photo,
        services = services.map { it.toServiceModel() },
        createdAt = OffsetDateTime.parse(createdAt).toLocalDateTime().atZone(ZoneId.systemDefault()),
        updatedAt = OffsetDateTime.parse(updatedAt).toLocalDateTime().atZone(ZoneId.systemDefault())
    )
}
