package esi.roadside.assistance.client.auth.presentation.screens.signup

import android.net.Uri
import esi.roadside.assistance.client.auth.presentation.screens.login.InputError

data class SignupUiState(
    val fullName: String = "",
    val fullNameError: InputError? = null,
    val phoneNumber: String = "",
    val phoneNumberError: InputError? = null,
    val email: String = "",
    val emailError: InputError? = null,
    val password: String = "",
    val passwordError: InputError? = null,
    val passwordHidden: Boolean = true,
    val confirmPassword: String = "",
    val confirmPasswordError: InputError? = null,
    val confirmPasswordHidden: Boolean = true,
    val image: Uri? = null,
    val verifyEmailCode: String = "",
    val verifyEmailCodeError: InputError? = null,
    val uploadProgress: Float = 0f,
    val loading: Boolean = false,
)
