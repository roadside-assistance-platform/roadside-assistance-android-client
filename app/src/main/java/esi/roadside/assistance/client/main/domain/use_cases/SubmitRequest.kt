package esi.roadside.assistance.client.main.domain.use_cases

import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.repository.MainRepo

class SubmitRequest(private val mainRepo: MainRepo) {
    suspend operator fun invoke(request: AssistanceRequestModel): Result<ServiceModel, DomainError> = mainRepo.submitRequest(request)
}