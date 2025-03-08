package esi.roadside.assistance.client.auth.domain.models

data class UpdateRequest(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val photo: String?
)
