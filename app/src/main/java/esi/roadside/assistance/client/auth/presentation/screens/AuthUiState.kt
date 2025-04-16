package esi.roadside.assistance.client.auth.presentation.screens

import esi.roadside.assistance.client.core.data.networking.DomainError

data class AuthUiState(
    val errorDialogVisible: Boolean = false,
    val error: DomainError? = null,
    val loading: Boolean = true,
    val action: Pair<Int, () -> Unit>? = null,
)