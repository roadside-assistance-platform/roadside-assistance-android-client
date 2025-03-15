package esi.roadside.assistance.client.core.presentation.util

import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.auth.util.AuthError
import esi.roadside.assistance.client.main.presentation.NavRoutes as MainNavRoutes

sealed interface Event {
    data class AuthNavigate(val route: NavRoutes): Event
    data class AuthShowError(val error: AuthError): Event
    data class MainNavigate(val route: MainNavRoutes): Event
    data object ImageUploadError: Event
    data object LaunchMainActivity: Event
}