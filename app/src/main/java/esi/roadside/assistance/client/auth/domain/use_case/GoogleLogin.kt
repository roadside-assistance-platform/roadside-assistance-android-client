package esi.roadside.assistance.client.auth.domain.use_case

import androidx.credentials.GetCredentialResponse
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class GoogleLogin(private val authRepo: AuthRepo) {
    suspend operator fun invoke(result: GetCredentialResponse) = authRepo.googleLogin(result)
}