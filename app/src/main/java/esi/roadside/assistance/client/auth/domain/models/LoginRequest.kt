package esi.roadside.assistance.client.auth.domain.models

data class LoginRequest(
    val username: String,
    val password: String
)