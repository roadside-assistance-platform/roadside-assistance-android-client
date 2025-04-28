package esi.roadside.assistance.client.main.domain.models.geocoding

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
) {
    override fun toString() = "$longitude,$latitude"
}