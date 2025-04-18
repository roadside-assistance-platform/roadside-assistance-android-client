package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class ResetPassword(private val authRepo: AuthRepo) {
    suspend operator fun invoke(email: String) = authRepo.resetPassword(email)
}