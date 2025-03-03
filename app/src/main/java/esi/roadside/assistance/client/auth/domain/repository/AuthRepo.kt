package esi.roadside.assistance.client.auth.domain.repository

import esi.roadside.assistance.client.auth.domain.models.LoginRequest
import esi.roadside.assistance.client.auth.domain.models.SignupRequest
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.NetworkError
import esi.roadside.assistance.client.core.domain.util.Result

interface AuthRepo {
    suspend fun login(request: LoginRequest): Result<ClientModel,NetworkError>
    suspend fun signup(request: SignupRequest): Result<ClientModel,NetworkError>
    suspend fun resetPassword(email: String): Result<ClientModel,NetworkError>
}