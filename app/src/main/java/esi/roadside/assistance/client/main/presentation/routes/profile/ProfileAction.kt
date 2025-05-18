package esi.roadside.assistance.client.main.presentation.routes.profile

import esi.roadside.assistance.client.main.presentation.models.ClientUi


sealed interface ProfileAction {
    data object EnableProfileEditing: ProfileAction
    data object CancelProfileEditing: ProfileAction
    data object ConfirmProfileEditing: ProfileAction
    data class EditClient(val client: ClientUi): ProfileAction
}