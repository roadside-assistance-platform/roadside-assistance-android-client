package esi.roadside.assistance.client.main.presentation.routes.home

import com.mapbox.geojson.Point
import esi.roadside.assistance.client.main.data.dto.mapbox.RouteX
import esi.roadside.assistance.client.main.domain.models.ProviderInfo
import esi.roadside.assistance.client.main.presentation.ClientState

data class HomeUiState(
    val clientState: ClientState = ClientState.IDLE,
    val time: Long = 5 * 60 * 1000,
    val location: Point? = null,
    val providerInfo: ProviderInfo? = null,
    val providerLocation: Point? = null,
    val directions: RouteX? = null,
    val servicePrice: Int = 0,
    val loading: Boolean = false
)