package esi.roadside.assistance.client.auth.domain.models

data class SignupRequest(
    val email: String,
    val password: String,
    val role: String = "client"
)
