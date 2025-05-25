package esi.roadside.assistance.client.main.presentation.routes.home

import com.mapbox.geojson.Point
import esi.roadside.assistance.client.main.data.dto.mapbox.RouteX
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.domain.models.ProviderInfo
import esi.roadside.assistance.client.main.presentation.ClientState

data class HomeUiState(
    val location: LocationModel? = null,
    val providerLocation: Point? = null,
    val message: String = "",
    val directions: RouteX? = null,
    val servicePrice: Int = 0,
    val loading: Boolean = false,
    val servicesLoading: Boolean = false
)