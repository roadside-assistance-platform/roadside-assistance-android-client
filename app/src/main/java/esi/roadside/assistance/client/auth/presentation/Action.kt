package esi.roadside.assistance.client.auth.presentation

import android.net.Uri
import androidx.credentials.GetCredentialResponse
import esi.roadside.assistance.client.auth.util.AuthError

sealed interface Action {
    data class SetLoginEmail(val email: String): Action
    data class SetLoginPassword(val password: String): Action
    data object ToggleLoginPasswordHidden: Action
    data object ToggleSignupPasswordHidden: Action
    data object ToggleSignupConfirmPasswordHidden: Action

    data class SetSignupFullName(val fullName: String): Action
    data class SetSignupPhoneNumber(val phoneNumber: String): Action
    data class SetSignupEmail(val email: String): Action
    data class SetSignupPassword(val password: String): Action
    data class SetSignupConfirmPassword(val confirmPassword: String): Action
    data class SetSignupImage(val image: Uri?): Action
    data class SetVerifyEmailCode(val code: String): Action


    data object GoToLogin: Action
    data object GoToSignup: Action
    data object GoToForgotPassword: Action

    data object GoToGoogleLogin: Action
    data class GoogleLogin(val result: GetCredentialResponse): Action
    data class GoogleOldLogin(val idToken: String): Action

    data object Login: Action
    data object Signup: Action
    data object Send: Action

    data class SetResetPasswordEmail(val email: String): Action

    data class SetCode(val code: String): Action
    data object SkipVerification: Action

    data class ShowAuthError(val error: AuthError): Action
    data object HideAuthError: Action
}