package esi.roadside.assistance.client.auth.domain.models

data class SignupRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val phoneNumber: String
)
