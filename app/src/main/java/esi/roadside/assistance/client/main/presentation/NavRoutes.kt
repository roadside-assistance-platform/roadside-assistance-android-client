package esi.roadside.assistance.client.main.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object Home : NavRoutes()

    @Serializable
    data object RequestAssistance : NavRoutes()

    @Serializable
    data object Notifications : NavRoutes()

    @Serializable
    data class Notification(val id: String) : NavRoutes()

    @Serializable
    data object Profile : NavRoutes()

    @Serializable
    data object Settings : NavRoutes()
}