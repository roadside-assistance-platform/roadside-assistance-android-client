package esi.roadside.assistance.client.main.presentation.routes.profile

import esi.roadside.assistance.client.main.presentation.models.ClientUi

data class ProfileUiState(
    val client: ClientUi = ClientUi(),
    val editClient: ClientUi = ClientUi(),
    val enableEditing: Boolean = false,
)
