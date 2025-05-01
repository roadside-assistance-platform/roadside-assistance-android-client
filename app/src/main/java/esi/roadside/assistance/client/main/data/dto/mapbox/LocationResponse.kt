package esi.roadside.assistance.client.main.data.dto.mapbox

data class LocationResponse(
    val bbox: List<Int>,
    val limit: Int,
    val q: String,
    val types: List<String>
)