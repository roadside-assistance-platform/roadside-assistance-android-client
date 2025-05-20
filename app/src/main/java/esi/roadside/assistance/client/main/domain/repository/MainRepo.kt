package esi.roadside.assistance.client.main.domain.repository

import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.CompletionResponse
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.models.CompletionRequest

interface MainRepo {
    suspend fun submitRequest(request: AssistanceRequestModel): Result<ServiceModel, DomainError>
    suspend fun rate(serviceId: String, rating: Double?): Result<Any, DomainError>
    suspend fun completionRequest(request: CompletionRequest): Result<CompletionResponse, DomainError>
    suspend fun logout()
}