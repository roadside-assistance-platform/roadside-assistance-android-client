package esi.roadside.assistance.client.main.data.dto.mapbox

import kotlinx.serialization.Serializable

@Serializable
data class JsonDirectionsResponse(
    val code: String,
    val routes: List<RouteX>,
    val uuid: String,
    val waypoints: List<Waypoint>
)