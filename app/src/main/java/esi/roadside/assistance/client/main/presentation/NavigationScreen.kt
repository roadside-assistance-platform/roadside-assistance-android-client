package esi.roadside.assistance.client.main.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import esi.roadside.assistance.client.main.presentation.routes.home.HomeScreen
import esi.roadside.assistance.client.main.presentation.routes.profile.ProfileScreen
import esi.roadside.assistance.client.main.presentation.routes.settings.SettingsScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
fun NavigationScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onAction: (Action) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentNavRoute =
        currentRoute?.let { route ->
            Routes
                .entries
                .firstOrNull {
                    it.route.javaClass.kotlin.qualifiedName?.contains(route) == true
                }
        } ?: Routes.HOME
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = { NavigationBar(navController, currentNavRoute) }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home,
            enterTransition = { materialFadeThroughIn() },
            exitTransition = { materialFadeThroughOut() },
            modifier = Modifier.fillMaxSize().padding(it),
        ) {
            composable<NavRoutes.Home> {
                HomeScreen(onAction = onAction)
            }
            composable<NavRoutes.Profile> {
                ProfileScreen()
            }
            composable<NavRoutes.Settings> {
                SettingsScreen()
            }
        }
    }
}