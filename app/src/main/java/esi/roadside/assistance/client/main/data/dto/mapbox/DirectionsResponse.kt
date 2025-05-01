package esi.roadside.assistance.client.main.data.dto.mapbox

import kotlinx.serialization.Serializable

@Serializable
data class DirectionsResponse(
    val routes: List<Route>,
)
