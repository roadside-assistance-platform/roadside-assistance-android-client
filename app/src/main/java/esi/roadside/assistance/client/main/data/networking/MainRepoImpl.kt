package esi.roadside.assistance.client.main.data.networking

import esi.roadside.assistance.client.auth.data.PersistentCookieStorage
import esi.roadside.assistance.client.core.data.Endpoints
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.data.networking.safeCall
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.core.domain.util.map
import esi.roadside.assistance.client.main.data.dto.UpdateResponse
import esi.roadside.assistance.client.main.domain.models.AssistanceRequestModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import esi.roadside.assistance.client.main.domain.repository.MainRepo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class MainRepoImpl(
    private val client: HttpClient,
    private val storage: PersistentCookieStorage,
): MainRepo {
    override suspend fun submitRequest(request: AssistanceRequestModel): Result<ServiceModel, DomainError> =
        safeCall<UpdateResponse> {
            client.post(constructUrl(Endpoints.SERVICE_CREATE)) {
                setBody(request.toAssistanceRequest())
            }.body()
        }.map {
            it.data.service.toServiceModel()
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
    override suspend fun finishRequest(
        serviceId: String,
    ): Result<ServiceModel, DomainError> {
        return safeCall<UpdateResponse> {
            client.put(constructUrl("${Endpoints.SERVICE_UPDATE}$serviceId")) {
                val json = """
                    {
                        "done": true
                    }
                """.trimIndent()
                setBody(json)
            }.body()
        }.map {
            it.data.service.toServiceModel()
        }
    }

    override suspend fun logout() = storage.deleteCookie()
}