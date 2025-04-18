package esi.roadside.assistance.client.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SendEmailModel(
    val email: String
)
