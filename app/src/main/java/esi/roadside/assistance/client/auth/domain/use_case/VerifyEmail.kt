package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.SendEmailModel
import esi.roadside.assistance.client.auth.domain.models.VerifyEmailModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class VerifyEmail(private val authRepo: AuthRepo) {
    suspend operator fun invoke(request: VerifyEmailModel) = authRepo.verifyEmail(request)
}