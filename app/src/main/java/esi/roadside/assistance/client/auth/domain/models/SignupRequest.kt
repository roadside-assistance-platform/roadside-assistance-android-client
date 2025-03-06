package esi.roadside.assistance.client.auth.domain.models

data class SignupRequest(
    val username: String,
    val password: String,
    val role: String = "client"
)
