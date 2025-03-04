package esi.roadside.assistance.client.core.presentation.util

import esi.roadside.assistance.client.auth.presentation.NavRoutes

sealed interface Event {
    data class AuthNavigate(val route: NavRoutes): Event
    data object LaunchMainActivity: Event
}