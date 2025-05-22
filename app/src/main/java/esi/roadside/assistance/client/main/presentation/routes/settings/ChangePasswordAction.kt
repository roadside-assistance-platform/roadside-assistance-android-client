package esi.roadside.assistance.provider.main.presentation.routes.settings

sealed interface ChangePasswordAction {
//    data class SetCurrentPassword(val value: String): ChangePasswordAction
//    data class SetCurrentPasswordVisible(val value: Boolean): ChangePasswordAction
    data class SetNewPassword(val value: String): ChangePasswordAction
    data class SetNewPasswordVisible(val value: Boolean): ChangePasswordAction
    data class SetConfirmNewPassword(val value: String): ChangePasswordAction
    data class SetConfirmNewPasswordVisible(val value: Boolean): ChangePasswordAction
    data object Confirm: ChangePasswordAction
}