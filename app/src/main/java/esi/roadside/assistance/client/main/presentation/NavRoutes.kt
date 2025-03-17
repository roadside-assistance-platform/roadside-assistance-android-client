package esi.roadside.assistance.client.main.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes(val ordinal: Int) {
    @Serializable
    data object Home : NavRoutes(0)

    @Serializable
    data object Map : NavRoutes(1)

    @Serializable
    data object RequestAssistance : NavRoutes(1)

    @Serializable
    data object Notifications : NavRoutes(2)

    @Serializable
    data object NotificationsList : NavRoutes(3)

    @Serializable
    data class Notification(val id: String) : NavRoutes(4)

    @Serializable
    data object Profile : NavRoutes(5)

    @Serializable
    data object Settings : NavRoutes(6)

    @Serializable
    data object SettingsList : NavRoutes(7)

    @Serializable
    data object ChangePassword : NavRoutes(8)

    @Serializable
    data object DeleteAccount : NavRoutes(8)

    @Serializable
    data object CustomizeApp : NavRoutes(8)

    @Serializable
    data object Language : NavRoutes(8)

    @Serializable
    data object About : NavRoutes(8)

    @Serializable
    data object TermsOfService : NavRoutes(8)

    @Serializable
    data object PrivacyPolicy : NavRoutes(8)

    @Serializable
    data object Help : NavRoutes(8)
}