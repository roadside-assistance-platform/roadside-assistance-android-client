package esi.roadside.assistance.client.auth.domain.models

import esi.roadside.assistance.client.auth.data.dto.SignupRequest

data class SignupModel(
    val email: String,
    val password: String,
    val fullName: String,
    val phone: String,
    val photo: String,
) {
    fun toSignupRequest() = SignupRequest(
        email = email,
        password = password,
        fullName = fullName,
        phone = phone,
        photo = photo
    )
}
