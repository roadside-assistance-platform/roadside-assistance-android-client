package esi.roadside.assistance.client.auth.data.dto

import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.core.data.dto.Client
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val Client: Client
) {
    fun toLoginResponseModel() = LoginResponseModel(
        message = message,
        client = Client.toClientModel()
    )
}
