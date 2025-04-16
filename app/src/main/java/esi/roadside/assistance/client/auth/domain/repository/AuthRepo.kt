package esi.roadside.assistance.client.auth.domain.repository

import androidx.credentials.GetCredentialResponse
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.Result

interface AuthRepo {
    suspend fun login(request: LoginRequestModel): Result<LoginResponseModel, DomainError>
    suspend fun signup(request: SignupModel): Result<LoginResponseModel, DomainError>
    suspend fun resetPassword(email: String): Result<ClientModel, DomainError>
    suspend fun update(request: UpdateModel): Result<ClientModel, DomainError>
    suspend fun authHome(): Result<Boolean, DomainError>
    suspend fun googleLogin(result: GetCredentialResponse): Result<ClientModel, DomainError>
    suspend fun googleOldLogin(idToken: String): Result<LoginResponseModel, DomainError>
    suspend fun authenticate()
}