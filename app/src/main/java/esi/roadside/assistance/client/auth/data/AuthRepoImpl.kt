package esi.roadside.assistance.client.auth.data

import esi.roadside.assistance.client.auth.domain.models.LoginRequest
import esi.roadside.assistance.client.auth.domain.models.SignupRequest
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.data.dto.Client
import io.ktor.client.HttpClient

class AuthRepoImpl(
    private val client: HttpClient,
): AuthRepo {
    override suspend fun login(request: LoginRequest): Result<Client> {
        TODO("Not yet implemented")
    }

    override suspend fun signup(request: SignupRequest): Result<Client> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String): Result<Client> {
        TODO("Not yet implemented")
    }
}