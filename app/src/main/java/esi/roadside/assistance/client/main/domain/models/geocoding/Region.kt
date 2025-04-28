package esi.roadside.assistance.client.main.domain.models.geocoding

import kotlinx.serialization.Serializable

@Serializable
data class Region(
    val mapbox_id: String,
    val name: String,
    val region_code: String,
    val region_code_full: String,
    val wikidata_id: String
)