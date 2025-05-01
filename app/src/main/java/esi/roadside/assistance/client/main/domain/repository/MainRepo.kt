package esi.roadside.assistance.client.main.domain.repository

import esi.roadside.assistance.client.core.data.dto.Service
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.models.SubmitResponseModel
import esi.roadside.assistance.client.main.domain.models.geocoding.GeocodingResponseModel

interface MainRepo {
    suspend fun submitRequest(request: AssistanceRequestModel): Result<ServiceModel, DomainError>
    suspend fun finishRequest(serviceId: String): Result<ServiceModel, DomainError>
    suspend fun rate(serviceId: String, rating: Double?): Result<Any, DomainError>
    suspend fun logout()
}