package esi.roadside.assistance.client.auth.domain.repository

import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import esi.roadside.assistance.client.auth.domain.models.HomeRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.util.AuthError
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.Result

interface AuthRepo {
    suspend fun login(request: LoginRequestModel): Result<LoginResponseModel, AuthError>
    suspend fun signup(request: SignupModel): Result<ClientModel, AuthError>
    suspend fun resetPassword(email: String): Result<ClientModel, AuthError>
    suspend fun update(request: UpdateModel): Result<ClientModel, AuthError>
    suspend fun home(): Result<Boolean, AuthError>
    suspend fun googleLogin(result: GetCredentialResponse): Result<ClientModel, AuthError>
    suspend fun googleOldLogin(idToken: String): Result<LoginResponseModel, AuthError>
    suspend fun authenticate()
}