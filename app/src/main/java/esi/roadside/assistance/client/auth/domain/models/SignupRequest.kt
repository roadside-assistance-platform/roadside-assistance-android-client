package esi.roadside.assistance.client.auth.domain.models

data class SignupRequest(
    val fullName: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val phoneNumber: String,
    val photo: String?
)
