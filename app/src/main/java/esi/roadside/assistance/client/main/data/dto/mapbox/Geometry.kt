package esi.roadside.assistance.client.main.data.dto.mapbox

import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)