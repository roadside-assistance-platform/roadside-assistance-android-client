package esi.roadside.assistance.client.auth.presentation.screens.reset_password


sealed interface ResetPasswordAction {
    data class SetEmail(val email: String): ResetPasswordAction
    data class SetPassword(val password: String): ResetPasswordAction
    data class SetPasswordHidden(val hidden: Boolean): ResetPasswordAction
    data class SetConfirmPassword(val confirmPassword: String): ResetPasswordAction
    data class SetConfirmPasswordHidden(val hidden: Boolean): ResetPasswordAction
    data object Send: ResetPasswordAction
    data object Verify: ResetPasswordAction
    data object ResetPassword: ResetPasswordAction
}