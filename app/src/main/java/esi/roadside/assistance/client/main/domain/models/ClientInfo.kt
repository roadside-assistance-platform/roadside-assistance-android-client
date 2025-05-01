package esi.roadside.assistance.client.main.domain.models

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime


@Serializable
data class ClientInfo(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val photo: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
) {
    fun toClientInfoModel() = ClientInfoModel(
        id = id,
        fullName = fullName,
        email = email,
        phone = phone,
        photo = photo,
        createdAt = ZonedDateTime.parse(createdAt),
        updatedAt = ZonedDateTime.parse(updatedAt),
    )
}


data class ClientInfoModel(
    val id: String = "",
    val fullName: String,
    val email: String,
    val phone: String,
    val photo: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
