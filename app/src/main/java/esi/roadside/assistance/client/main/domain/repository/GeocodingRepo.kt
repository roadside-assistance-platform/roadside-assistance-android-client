package esi.roadside.assistance.client.main.domain.repository

import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.data.dto.mapbox.JsonDirectionsResponse
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.geocoding.GeocodingResponseModel

interface GeocodingRepo {
    suspend fun geocoding(query: String): Result<GeocodingResponseModel, DomainError>
    suspend fun getLocationString(request: LocationModel): Result<String, DomainError>
    suspend fun getDistance(request: Pair<LocationModel, LocationModel>): Result<JsonDirectionsResponse, DomainError>
}