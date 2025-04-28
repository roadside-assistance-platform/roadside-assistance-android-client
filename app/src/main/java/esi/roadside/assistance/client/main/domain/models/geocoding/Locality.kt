package esi.roadside.assistance.client.main.domain.models.geocoding

import kotlinx.serialization.Serializable

@Serializable
data class Locality(
    val mapbox_id: String,
    val name: String,
    val wikidata_id: String = ""
)