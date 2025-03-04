package esi.roadside.assistance.client.main.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object Home : NavRoutes()

    @Serializable
    data object Profile : NavRoutes()

    @Serializable
    data object Settings : NavRoutes()
}