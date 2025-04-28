package esi.roadside.assistance.client.main.domain.models.geocoding

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val country_code: String,
    val country_code_alpha_3: String,
    val mapbox_id: String,
    val name: String,
    val wikidata_id: String
)