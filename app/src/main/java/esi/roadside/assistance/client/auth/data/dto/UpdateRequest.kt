package esi.roadside.assistance.client.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequest(
    val fullName: String,
    val phoneNumber: String,
    val photo: String?
)