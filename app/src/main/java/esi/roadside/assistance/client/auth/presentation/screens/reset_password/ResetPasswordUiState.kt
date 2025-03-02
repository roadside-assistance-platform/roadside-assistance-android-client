package esi.roadside.assistance.client.auth.presentation.screens.reset_password

data class ResetPasswordUiState(
    val email: String = "",
    val emailError: Boolean = false,
    val code: String = "",
    val codeError: Boolean = false,
    val sent: Boolean = false
)
