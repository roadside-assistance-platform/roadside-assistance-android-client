package esi.roadside.assistance.client.main.data.dto.mapbox

import kotlinx.serialization.Serializable

@Serializable
data class Intersection(
    val admin_index: Int,
    val bearings: List<Int>,
    val duration: Double,
    val entry: List<Boolean>,
    val geometry_index: Int,
    val `in`: Int,
    val is_urban: Boolean,
    val location: List<Double>,
    val mapbox_streets_v8: MapboxStreetsV8,
    val `out`: Int,
    val railway_crossing: Boolean,
    val turn_duration: Double,
    val turn_weight: Double,
    val weight: Double
)