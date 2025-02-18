package esi.roadside.assistance.client.main.domain.models

import esi.roadside.assistance.client.main.data.mappers.toLocation

data class LocationModel(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun fromString(location: String) = location.toLocation()
    }

    override fun toString() = "${latitude},${longitude}"
}
