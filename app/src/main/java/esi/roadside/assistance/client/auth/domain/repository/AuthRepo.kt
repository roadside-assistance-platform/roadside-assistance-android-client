package esi.roadside.assistance.client.auth.domain.repository

import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.util.AuthError
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.NetworkError
import esi.roadside.assistance.client.core.domain.util.Result

interface AuthRepo {
    suspend fun login(request: LoginRequestModel): Result<LoginResponseModel, AuthError>
    suspend fun signup(request: SignupModel): Result<SignupModel, AuthError>
    suspend fun resetPassword(email: String): Result<ClientModel, NetworkError>
    suspend fun update(request: UpdateModel): Result<ClientModel, NetworkError>
    suspend fun googleLogin(): Result<ClientModel,NetworkError>
    suspend fun authenticate()

}