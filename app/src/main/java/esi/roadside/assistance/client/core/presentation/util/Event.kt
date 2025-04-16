package esi.roadside.assistance.client.core.presentation.util

import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.main.presentation.NavRoutes as MainNavRoutes

sealed interface Event {
    data class AuthNavigate(val route: NavRoutes): Event
    data object AuthShowNoInternet: Event
    data object LaunchGoogleSignIn: Event
    data class AuthShowError(val error: DomainError): Event
    data class MainNavigate(val route: MainNavRoutes): Event
    data object ImageUploadError: Event
    data object LaunchMainActivity: Event
    data class ShowMainActivityMessage(val text: Int): Event
    data object ShowRequestAssistance: Event
    data object HideRequestAssistance: Event
}