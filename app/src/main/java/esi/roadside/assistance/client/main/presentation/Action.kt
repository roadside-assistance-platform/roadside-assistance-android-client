package esi.roadside.assistance.client.main.presentation

import com.mapbox.geojson.Point

sealed interface Action {
        data object RequestService: Action
        data class SetLocation(val location: Point?): Action
        data object OpenNotifications: Action
}