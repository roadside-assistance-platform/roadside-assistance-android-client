package esi.roadside.assistance.client.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailModel(
    val email: String,
    val code: String,
)
