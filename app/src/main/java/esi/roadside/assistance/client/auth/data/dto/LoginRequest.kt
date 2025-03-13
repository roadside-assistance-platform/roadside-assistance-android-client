package esi.roadside.assistance.client.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)