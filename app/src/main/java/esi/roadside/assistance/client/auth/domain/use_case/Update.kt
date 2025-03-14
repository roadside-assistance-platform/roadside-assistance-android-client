package esi.roadside.assistance.client.auth.domain.use_case

import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo

class Update(private val authRepo: AuthRepo) {
    suspend operator fun invoke(updateRequest: UpdateModel) = authRepo.update(updateRequest)
}