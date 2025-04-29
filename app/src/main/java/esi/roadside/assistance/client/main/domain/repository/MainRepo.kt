package esi.roadside.assistance.client.main.domain.repository

import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.SubmitResponseModel
import esi.roadside.assistance.client.main.domain.models.geocoding.GeocodingResponseModel

interface MainRepo {
    suspend fun submitRequest(request: AssistanceRequestModel): Result<SubmitResponseModel, DomainError>
    suspend fun finishRequest(serviceId: String, rating: Double?): Result<Any, DomainError>
    suspend fun logout()
    suspend fun geocoding(query: String): Result<GeocodingResponseModel, DomainError>
}