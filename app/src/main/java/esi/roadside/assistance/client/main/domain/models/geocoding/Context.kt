package esi.roadside.assistance.client.main.domain.models.geocoding

import kotlinx.serialization.Serializable

@Serializable
data class Context(
    val country: Country,
    val locality: Locality? = null,
    val place: Place? = null,
    val region: Region? = null
)