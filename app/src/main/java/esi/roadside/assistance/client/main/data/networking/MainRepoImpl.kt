package esi.roadside.assistance.client.main.data.networking

import esi.roadside.assistance.client.auth.data.PersistentCookieStorage
import esi.roadside.assistance.client.core.data.Endpoints
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.data.networking.safeCall
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.map
import esi.roadside.assistance.client.main.data.dto.FetchServicesDto
import esi.roadside.assistance.client.main.data.dto.UpdateResponse
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.CompletionRequest
import esi.roadside.assistance.client.main.domain.models.CompletionResponse
import esi.roadside.assistance.client.main.domain.models.FetchServicesModel
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.repository.GeocodingRepo
import esi.roadside.assistance.client.main.domain.repository.MainRepo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class MainRepoImpl(
    private val client: HttpClient,
    private val storage: PersistentCookieStorage,
    private val geocodingRepo: GeocodingRepo
): MainRepo {
    override suspend fun submitRequest(request: AssistanceRequestModel): Result<ServiceModel, DomainError> =
        safeCall<UpdateResponse> {
            client.post(constructUrl(Endpoints.SERVICE_CREATE)) {
                setBody(request.toAssistanceRequest())
            }.body()
        }.map {
            it.data.service.toServiceModel().let { model ->
                model.copy(
                    serviceLocation = LocationModel(model.serviceLocation.latitude, model.serviceLocation.longitude)
                )
            }
        }

    override suspend fun rate(
        serviceId: String, rating: Double?
    ): Result<ServiceModel, DomainError> {
        return safeCall<UpdateResponse> {
            client.put(constructUrl("${Endpoints.SERVICE_UPDATE}$serviceId")) {
                rating?.let {
                    val json = """
                    {
                        "rating": $rating
                    }
                """.trimIndent()
                    setBody(json)
                }
            }.body()
        }.map {
            it.data.service.toServiceModel()
        }
    }

    override suspend fun completionRequest(request: CompletionRequest): Result<CompletionResponse, DomainError> {
        return safeCall<CompletionResponse> {
            client.post(constructUrl(Endpoints.COMPLETION_REQUEST)) {
                setBody(request)
            }.body()
        }
    }

    override suspend fun fetchServices(id: String): Result<FetchServicesModel, DomainError> {
        return safeCall<FetchServicesDto> {
            client.get(constructUrl("${Endpoints.SERVICES}$id")).body()
        }.map {
            FetchServicesModel(
                status = it.status,
                data = it.data.toDomainModel {
//                    var location = ""
//                    geocodingRepo.getLocationString(it.serviceLocation.toLocation()).onSuccess { locationString ->
//                        location = locationString
//                    }
//                    location
                    ""
                }.also {
                    it.copy(services = it.services.sortedByDescending { service -> service.createdAt })
                }
            )
        }
    }

    override suspend fun logout() = storage.deleteCookie()
}