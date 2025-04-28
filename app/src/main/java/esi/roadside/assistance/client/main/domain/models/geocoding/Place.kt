package esi.roadside.assistance.client.main.domain.models.geocoding

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val mapbox_id: String,
    val name: String,
    val short_code: String = "",
    val wikidata_id: String = ""
)