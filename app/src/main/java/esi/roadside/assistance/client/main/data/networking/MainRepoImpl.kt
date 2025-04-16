package esi.roadside.assistance.client.main.data.networking

import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.data.networking.safeCall
import esi.roadside.assistance.client.core.domain.util.Result
import esi.roadside.assistance.client.main.domain.models.SubmitRequestModel
import esi.roadside.assistance.client.main.domain.models.SubmitResponseModel
import esi.roadside.assistance.client.main.domain.repository.MainRepo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class MainRepoImpl(
    private val client: HttpClient,
): MainRepo {
    override suspend fun submitRequest(request: SubmitRequestModel): Result<SubmitResponseModel, DomainError> =
        safeCall<SubmitResponseModel> {
            client.post(constructUrl("/service/create")) {
                setBody(request)
            }.body()
        }
}