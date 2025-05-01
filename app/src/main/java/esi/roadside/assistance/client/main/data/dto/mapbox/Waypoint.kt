package esi.roadside.assistance.client.main.data.dto.mapbox

import kotlinx.serialization.Serializable

@Serializable
data class Waypoint(
    val distance: Double,
    val location: List<Double>,
    val name: String
)