package esi.roadside.assistance.client.auth.domain.models

import esi.roadside.assistance.client.core.domain.model.ClientModel

data class LoginResponseModel(
    val message: String? = null,
    val client: ClientModel
)
