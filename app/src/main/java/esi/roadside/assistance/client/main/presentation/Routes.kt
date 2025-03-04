package esi.roadside.assistance.client.main.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import esi.roadside.assistance.client.R
import androidx.compose.ui.graphics.vector.ImageVector

enum class Routes(val route: NavRoutes, val title: Int, val icon: ImageVector) {
    HOME(NavRoutes.Home, R.string.home, Icons.Default.Home),
    PROFILE(NavRoutes.Profile, R.string.profile, Icons.Default.Person),
    SETTINGS(NavRoutes.Settings, R.string.settings, Icons.Default.Settings)
}