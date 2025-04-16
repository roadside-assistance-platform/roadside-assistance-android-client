package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.data.dto.LoginResponse
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.Result

class SignUp(private val authRepo: AuthRepo) {
    suspend operator fun invoke(signupRequest: SignupModel) = authRepo.signup(signupRequest)
}