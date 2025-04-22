package esi.roadside.assistance.client.main.presentation.routes.home

import com.mapbox.geojson.Point
import esi.roadside.assistance.client.main.presentation.ClientState

data class HomeUiState(
    val clientState: ClientState = ClientState.IDLE,
    val location: Point? = null,
)