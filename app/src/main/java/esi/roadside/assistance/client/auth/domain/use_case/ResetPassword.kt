package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.data.dto.Client
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.NetworkError
import esi.roadside.assistance.client.core.domain.util.Result

class ResetPassword(private val authRepo: AuthRepo) {
    suspend operator fun invoke(email: String): Result<ClientModel, NetworkError> =
        authRepo.resetPassword(email)
}