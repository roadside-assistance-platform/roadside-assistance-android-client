package esi.roadside.assistance.client.main.domain.models.geocoding

import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponseModel(
    val attribution: String,
    val features: List<Feature>,
    val type: String
)