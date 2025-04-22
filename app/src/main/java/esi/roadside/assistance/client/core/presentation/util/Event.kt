package esi.roadside.assistance.client.core.presentation.util

import android.app.Activity
import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.main.presentation.NavRoutes as MainNavRoutes

sealed interface Event {
    data class AuthNavigate(val route: NavRoutes): Event
    data object ExitToAuthActivity: Event
    data object AuthShowNoInternet: Event
    data object LaunchGoogleSignIn: Event
    data class AuthShowError(val error: DomainError): Event
    data class MainNavigate(val route: MainNavRoutes): Event
    data object ImageUploadError: Event
    data object LaunchMainActivity: Event
    data class ShowAuthActivityMessage(val text: Int): Event
    data class ShowMainActivityMessage(val text: Int): Event
    data class DismissSnackbar(val text: Int): Event
    data class ShowMainActivityActionSnackbar(val text: Int, val actionText: Int, val callback: () -> Unit): Event
    data object ShowRequestAssistance: Event
    data object HideRequestAssistance: Event
}