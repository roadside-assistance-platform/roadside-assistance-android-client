package esi.roadside.assistance.client.main.presentation.routes.home.search

import esi.roadside.assistance.client.main.domain.models.geocoding.GeocodingResponseModel

data class SearchState(
    val query: String = "",
    val result: GeocodingResponseModel? = null,
    val expanded: Boolean = false,
)