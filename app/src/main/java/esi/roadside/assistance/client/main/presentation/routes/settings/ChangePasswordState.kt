package esi.roadside.assistance.provider.main.presentation.routes.settings

import esi.roadside.assistance.provider.auth.presentation.screens.login.InputError

data class ChangePasswordState(
//    val currentPassword: String = "",
//    val currentPasswordVisible: Boolean = false,
//    val currentPasswordError: InputError? = null,
    val newPassword: String = "",
    val newPasswordVisible: Boolean = false,
    val newPasswordError: InputError? = null,
    val confirmNewPassword: String = "",
    val confirmNewPasswordVisible: Boolean = false,
    val confirmNewPasswordError: InputError? = null,
    val loading: Boolean = false,
)
