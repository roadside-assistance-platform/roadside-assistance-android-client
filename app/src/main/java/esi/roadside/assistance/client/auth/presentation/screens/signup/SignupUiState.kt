package esi.roadside.assistance.client.auth.presentation.screens.signup

import android.net.Uri

data class SignupUiState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val phoneNumberError: Boolean = false,
    val email: String = "",
    val emailError: Boolean = false,
    val password: String = "",
    val passwordError: Boolean = false,
    val passwordHidden: Boolean = true,
    val confirmPassword: String = "",
    val confirmPasswordError: Boolean = false,
    val confirmPasswordHidden: Boolean = true,
    val image: Uri? = null,
    val verifyEmailCode: String = "",
    val verifyEmailCodeError: Boolean = false,
    val uploadProgress: Float = 0f
)
