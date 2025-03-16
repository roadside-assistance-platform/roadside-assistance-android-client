package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class GoogleOldLogin(private val authRepo: AuthRepo) {
    suspend operator fun invoke(idToken: String) = authRepo.googleOldLogin(idToken)
}