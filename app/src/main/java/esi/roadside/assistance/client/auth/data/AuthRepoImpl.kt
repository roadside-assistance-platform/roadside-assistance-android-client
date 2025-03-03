package esi.roadside.assistance.client.auth.data

import esi.roadside.assistance.client.auth.domain.models.LoginRequest
import esi.roadside.assistance.client.auth.domain.models.SignupRequest
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.NetworkError
import esi.roadside.assistance.client.core.domain.util.Result
import io.ktor.client.HttpClient

class AuthRepoImpl(
    private val client: HttpClient,
): AuthRepo {
    override suspend fun login(request: LoginRequest): Result<ClientModel, NetworkError> {
        TODO("Not yet implemented")

    }

    override suspend fun signup(request: SignupRequest): Result<ClientModel, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String): Result<ClientModel, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun googleLogin(): Result<ClientModel, NetworkError> {
        TODO("Not yet implemented")
    }

}