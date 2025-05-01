package esi.roadside.assistance.client.main.data.dto.mapbox

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val distance: Double,
    val duration: Double,
    val geometry: String
)
