package esi.roadside.assistance.client.auth.domain.models

data class LoginRequest(
    val email: String,
    val password: String
)