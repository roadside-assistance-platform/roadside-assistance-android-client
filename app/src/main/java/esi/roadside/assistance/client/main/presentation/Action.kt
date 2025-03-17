package esi.roadside.assistance.client.main.presentation

import com.mapbox.geojson.Point
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.main.presentation.models.ClientUi

sealed interface Action {
        data class Navigate(val route: NavRoutes): Action
        data class SetLocation(val location: Point?): Action
        data class SelectCategory(val category: Categories): Action
        data class SetDescription(val description: String): Action
        data object SubmitRequest: Action
        data object EnableProfileEditing: Action
        data object CancelProfileEditing: Action
        data class EditClient(val client: ClientUi): Action
        data object ConfirmProfileEditing: Action
}