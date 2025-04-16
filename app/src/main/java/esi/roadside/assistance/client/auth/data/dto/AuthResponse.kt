package esi.roadside.assistance.client.auth.data.dto

import esi.roadside.assistance.client.auth.domain.models.AuthResponseModel
import esi.roadside.assistance.client.core.data.dto.Client
import kotlinx.serialization.Serializable

@Serializable
data class ResponseData(
    val user: Client,
)

@Serializable
data class AuthResponse(
    val status: String,
    val message: String? = null,
    val data: ResponseData
) {
    fun toLoginResponseModel() = AuthResponseModel(
        message = message,
        user = data.user.toClientModel()
    )
}
