package esi.roadside.assistance.client.main.data.networking

import esi.roadside.assistance.client.R
import android.content.Context
import esi.roadside.assistance.client.core.data.BaseUrls
import esi.roadside.assistance.client.core.data.Endpoints
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.data.networking.safeCall
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.data.dto.mapbox.JsonDirectionsResponse
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.geocoding.ReverseGeocodingResponse
import esi.roadside.assistance.client.main.domain.repository.GeocodingRepo
import esi.roadside.assistance.client.core.domain.util.map
import esi.roadside.assistance.client.main.domain.models.geocoding.GeocodingResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.collections.firstOrNull
import kotlin.text.isNotEmpty

class GeocodingRepoImpl(
    private val context: Context,
    private val client: HttpClient,
): GeocodingRepo {
    override suspend fun geocoding(query: String): Result<GeocodingResponseModel, DomainError> =
        safeCall<GeocodingResponseModel> {
            client.get(constructUrl(Endpoints.MAPBOX_GEOCODING, BaseUrls.MAPBOX_GEOCODING))  {
                url {
                    parameters.append("q", query)
                    parameters.append("access_token", context.getString(R.string.mapbox_access_token))
                }
            }.body()
        }

    override suspend fun getLocationString(request: LocationModel): Result<String, DomainError> {
        return safeCall<ReverseGeocodingResponse> {
            client.get(constructUrl(Endpoints.getLocationStringEndpoint(request.longitude, request.latitude, context), BaseUrls.MAPBOX_GEOCODING)).body()
        }.map {
            it.features.firstOrNull {
                it.properties.full_address.isNotEmpty()
            }?.properties?.full_address ?: ""
        }
    }

    override suspend fun getDistance(request: Pair<LocationModel, LocationModel>): Result<JsonDirectionsResponse, DomainError> {
        return safeCall<JsonDirectionsResponse> {
            client.get(
                constructUrl(
                    Endpoints.getRoutesEndpoint("driving", request, context),
                    BaseUrls.MAPBOX_DRIVING
                )
            ).body()
        }
    }
}