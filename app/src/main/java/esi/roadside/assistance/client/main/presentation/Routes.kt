package esi.roadside.assistance.client.main.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import esi.roadside.assistance.client.R

enum class Routes(val route: NavRoutes, val title: Int, val icon: ImageVector) {
    HOME(NavRoutes.Map, R.string.home, Icons.Default.Home),
    SERVICES(NavRoutes.ServicesList, R.string.notifications, Icons.Default.History),
    PROFILE(NavRoutes.Profile, R.string.profile, Icons.Default.Person),
    SETTINGS(NavRoutes.SettingsList, R.string.settings, Icons.Default.Settings)
}