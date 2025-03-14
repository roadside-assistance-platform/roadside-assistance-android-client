package esi.roadside.assistance.client.auth.domain.models

import esi.roadside.assistance.client.core.data.dto.Client

data class LoginResponseModel(
    val message: String ,
    val client: SignupModel
)
