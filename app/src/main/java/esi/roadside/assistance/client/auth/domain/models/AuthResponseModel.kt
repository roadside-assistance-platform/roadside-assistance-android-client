package esi.roadside.assistance.client.auth.domain.models

import esi.roadside.assistance.client.core.domain.model.ClientModel

data class AuthResponseModel(
    val message: String? = null,
    val user: ClientModel
)
