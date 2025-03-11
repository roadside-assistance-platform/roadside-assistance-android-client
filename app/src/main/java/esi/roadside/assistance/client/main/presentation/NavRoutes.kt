package esi.roadside.assistance.client.main.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object Home : NavRoutes()

    @Serializable
    data object Map : NavRoutes()

    @Serializable
    data object RequestAssistance : NavRoutes()

    @Serializable
    data object Notifications : NavRoutes()

    @Serializable
    data object NotificationsList : NavRoutes()

    @Serializable
    data class Notification(val id: String) : NavRoutes()

    @Serializable
    data object Profile : NavRoutes()

    @Serializable
    data object Settings : NavRoutes()

    @Serializable
    data object SettingsList : NavRoutes()

    @Serializable
    data object ChangePassword : NavRoutes()

    @Serializable
    data object DeleteAccount : NavRoutes()

    @Serializable
    data object CustomizeApp : NavRoutes()

    @Serializable
    data object Language : NavRoutes()

    @Serializable
    data object About : NavRoutes()

    @Serializable
    data object TermsOfService : NavRoutes()

    @Serializable
    data object PrivacyPolicy : NavRoutes()

    @Serializable
    data object Help : NavRoutes()
}