package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.NetworkError
import esi.roadside.assistance.client.core.domain.util.Result

class Update(private val authRepo: AuthRepo) {
    suspend operator fun invoke(updateRequest: UpdateModel): Result<ClientModel, NetworkError> =
        authRepo.update(updateRequest)
}