package esi.roadside.assistance.client.main.data.networking

import android.content.Context
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.data.PersistentCookieStorage
import esi.roadside.assistance.client.core.data.BaseUrls
import esi.roadside.assistance.client.core.data.Endpoints
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.data.networking.safeCall
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.SubmitResponseModel
import esi.roadside.assistance.client.main.domain.models.geocoding.GeocodingResponseModel
import esi.roadside.assistance.client.main.domain.repository.MainRepo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters

class MainRepoImpl(
    private val context: Context,
    private val client: HttpClient,
    private val storage: PersistentCookieStorage,
): MainRepo {
    override suspend fun submitRequest(request: AssistanceRequestModel): Result<SubmitResponseModel, DomainError> =
        safeCall<SubmitResponseModel> {
            client.post(constructUrl(Endpoints.SERVICE_CREATE)) {
                setBody(request.toAssistanceRequest())
            }.body()
        }

    override suspend fun logout() = storage.deleteCookie()
    override suspend fun geocoding(query: String): Result<GeocodingResponseModel, DomainError> =
        safeCall<GeocodingResponseModel> {
            client.get(constructUrl(Endpoints.MAPBOX_GEOCODING, BaseUrls.MAPBOX_GEOCODING))  {
                url {
                    parameters.append("q", query)
                    parameters.append("access_token", context.getString(R.string.mapbox_access_token))
                }
            }.body()
        }
}