package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.AuthResponseModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.util.Result

class Login(private val authRepo: AuthRepo) {
    suspend operator fun invoke(request: LoginRequestModel): Result<AuthResponseModel, DomainError>  =
        authRepo.login(request)
}