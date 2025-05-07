package esi.roadside.assistance.client.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetPassword(
    val email: String,
    val newPassword: String,
)