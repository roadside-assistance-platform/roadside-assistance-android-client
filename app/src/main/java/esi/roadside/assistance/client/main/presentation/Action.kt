package esi.roadside.assistance.client.main.presentation

import esi.roadside.assistance.client.main.domain.models.LocationModel

sealed interface Action {
    data class SetLocation(val location: LocationModel?): Action
    data class Navigate(val route: NavRoutes): Action
    data class SetMessage(val message: String): Action
    data object SendMessage: Action
    data object CancelRequest: Action
    data class CompleteRequest(val rating: Double?): Action
    data object Logout: Action
    data object WorkingFinished: Action
    data object FetchServices: Action
}