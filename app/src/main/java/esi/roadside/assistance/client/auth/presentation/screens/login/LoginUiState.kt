package esi.roadside.assistance.client.auth.presentation.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
    val passwordHidden: Boolean = true
)
