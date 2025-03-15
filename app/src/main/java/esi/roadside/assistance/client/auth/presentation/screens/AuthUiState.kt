package esi.roadside.assistance.client.auth.presentation.screens

import esi.roadside.assistance.client.auth.util.AuthError

data class AuthUiState(
    val errorDialogVisible: Boolean = false,
    val error: AuthError? = null,
)