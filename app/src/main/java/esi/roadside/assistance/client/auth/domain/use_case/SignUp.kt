package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class SignUp(private val authRepo: AuthRepo) {
    suspend operator fun invoke(signupRequest: SignupModel) = authRepo.signup(signupRequest)
}