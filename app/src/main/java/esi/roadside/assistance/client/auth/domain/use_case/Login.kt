package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.LoginResponseModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.auth.util.AuthError
import esi.roadside.assistance.client.core.domain.util.Result

class Login(private val authRepo: AuthRepo) {
    suspend operator fun invoke(request: LoginRequestModel): Result<LoginResponseModel, AuthError>  =
        authRepo.login(request)
}