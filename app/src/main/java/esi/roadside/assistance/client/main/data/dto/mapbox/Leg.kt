package esi.roadside.assistance.client.main.data.dto.mapbox

import kotlinx.serialization.Serializable

@Serializable
data class Leg(
    val admins: List<Admin>,
    val distance: Double,
    val duration: Double,
    val steps: List<Step>,
    val summary: String,
    val via_waypoints: List<String?>,
    val weight: Double
)