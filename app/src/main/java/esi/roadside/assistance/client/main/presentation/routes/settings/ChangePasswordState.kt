package esi.roadside.assistance.client.main.presentation.routes.settings

import esi.roadside.assistance.client.auth.presentation.InputError

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
