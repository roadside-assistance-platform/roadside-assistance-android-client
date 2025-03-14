package esi.roadside.assistance.client.auth.domain.models

import esi.roadside.assistance.client.auth.data.dto.LoginRequest

data class LoginRequestModel(
    val username: String,
    val password: String,
) {
    fun toLoginRequest() = LoginRequest(
        username = username,
        password = password
    )

}