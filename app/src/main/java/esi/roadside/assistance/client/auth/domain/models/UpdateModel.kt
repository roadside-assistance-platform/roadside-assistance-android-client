package esi.roadside.assistance.client.auth.domain.models

data class UpdateModel(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val photo: String?
)
