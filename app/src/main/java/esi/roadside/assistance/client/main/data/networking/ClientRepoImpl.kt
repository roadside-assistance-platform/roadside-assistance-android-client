package esi.roadside.assistance.client.main.data.networking

import esi.roadside.assistance.client.main.domain.repository.ClientRepo
import io.ktor.client.HttpClient

class ClientRepoImpl(
    private val client: HttpClient,
): ClientRepo {
}