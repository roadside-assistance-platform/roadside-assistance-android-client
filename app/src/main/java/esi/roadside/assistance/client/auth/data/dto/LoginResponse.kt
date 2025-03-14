package esi.roadside.assistance.client.auth.data.dto

import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val client: SignupRequest
) {
    fun toLoginResponseModel() = LoginResponseModel(
        message = message,
        client = client.toSignupModel()
    )
}
