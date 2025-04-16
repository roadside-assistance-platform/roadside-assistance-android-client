package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.main.domain.models.SubmitRequestModel
import esi.roadside.assistance.client.main.domain.repository.MainRepo

class SubmitRequest(private val mainRepo: MainRepo) {
    suspend operator fun invoke(request: SubmitRequestModel) = mainRepo.submitRequest(request)
}