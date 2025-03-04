package esi.roadside.assistance.client.main.presentation.routes.home

import com.mapbox.geojson.Point

data class HomeUiState(
    val location: Point? = null
)