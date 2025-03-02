package esi.roadside.assistance.client.auth.presentation.util

import esi.roadside.assistance.client.auth.presentation.NavRoutes

sealed interface Event {
    data class Navigate(val route: NavRoutes): Event
}