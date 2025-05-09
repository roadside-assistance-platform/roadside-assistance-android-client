package esi.roadside.assistance.client.main.presentation

import com.mapbox.geojson.Point
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.presentation.models.ClientUi

sealed interface Action {
        data class Navigate(val route: NavRoutes): Action
        data object ShowRequestAssistance: Action
        data object HideRequestAssistance: Action
        data class SetLocation(val location: Point?): Action
        data class SelectCategory(val category: Categories): Action
        data class SetDescription(val description: String): Action
        data object SubmitRequest: Action
        data object CancelRequest: Action
        data object Timeout: Action
        data class CompleteRequest(val rating: Double?): Action
        data object EnableProfileEditing: Action
        data object CancelProfileEditing: Action
        data class EditClient(val client: ClientUi): Action
        data object ConfirmProfileEditing: Action
        data object Logout: Action
        data object WorkingFinished: Action
}