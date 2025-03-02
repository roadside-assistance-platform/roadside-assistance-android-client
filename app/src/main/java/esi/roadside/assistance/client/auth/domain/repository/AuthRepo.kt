package esi.roadside.assistance.client.auth.domain.repository

import esi.roadside.assistance.client.auth.domain.models.LoginRequest
import esi.roadside.assistance.client.auth.domain.models.SignupRequest
import esi.roadside.assistance.client.core.data.dto.Client

interface AuthRepo {
    suspend fun login(request: LoginRequest): Result<Client>
    suspend fun signup(request: SignupRequest): Result<Client>
    suspend fun resetPassword(email: String): Result<Client>
}