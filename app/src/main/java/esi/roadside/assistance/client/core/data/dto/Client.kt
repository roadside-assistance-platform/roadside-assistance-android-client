package esi.roadside.assistance.client.core.data.dto

import esi.roadside.assistance.client.core.domain.model.ClientModel
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class Client(
    val id: String,
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String,
    val photo: String,
    val services: List<Service>,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toClientModel() = ClientModel(
        id = id,
        fullName = fullName,
        email = email,
        password = password,
        phone = phone,
        photo = photo,
        services = services.map { it.toServiceModel() },
        createdAt = Instant.ofEpochMilli(createdAt).atZone(ZoneId.systemDefault()),
        updatedAt = Instant.ofEpochMilli(updatedAt).atZone(ZoneId.systemDefault())
    )
}
