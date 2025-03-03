package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.SignupRequest
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.NetworkError
import esi.roadside.assistance.client.core.domain.util.Result

class SignUp(private val authRepo: AuthRepo) {
    suspend operator fun invoke(signupRequest: SignupRequest): Result<ClientModel, NetworkError> =
        authRepo.signup(signupRequest)
}