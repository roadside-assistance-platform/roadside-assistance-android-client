package esi.roadside.assistance.client.auth.presentation

import android.net.Uri
import esi.roadside.assistance.client.core.data.networking.DomainError

sealed interface Action {
    data object Initiate: Action

    data object NextStep: Action
    data object PreviousStep: Action
    data object Skip: Action

    data object GoToLogin: Action
    data object GoToSignup: Action
    data object GoToForgotPassword: Action

    data object SkipVerification: Action

    data class ShowAuthError(val error: DomainError): Action
    data object HideAuthError: Action
}