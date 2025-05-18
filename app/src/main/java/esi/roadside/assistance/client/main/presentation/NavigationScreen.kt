package esi.roadside.assistance.client.main.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import esi.roadside.assistance.client.core.util.intUpDownTransSpec
import esi.roadside.assistance.client.main.domain.models.LocationModel
import esi.roadside.assistance.client.main.presentation.routes.home.HomeScreen
import esi.roadside.assistance.client.main.presentation.routes.notifications.NotificationDetails
import esi.roadside.assistance.client.main.presentation.routes.notifications.NotificationsScreen
import esi.roadside.assistance.client.main.presentation.routes.profile.ProfileScreen
import esi.roadside.assistance.client.main.presentation.routes.settings.AboutScreen
import esi.roadside.assistance.client.main.presentation.routes.settings.CustomizeAppScreen
import esi.roadside.assistance.client.main.presentation.routes.settings.LanguageScreen
import esi.roadside.assistance.client.main.presentation.routes.settings.PrivacyPolicyScreen
import esi.roadside.assistance.client.main.presentation.routes.settings.SettingsScreen
import esi.roadside.assistance.client.main.presentation.routes.settings.TermsOfServiceScreen
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    mainViewModel: MainViewModel,
    onLocationChange: (LocationModel?) -> Unit,
    modifier: Modifier = Modifier,
    onRequest: () -> Unit,
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
    val isParent =
        currentRoute?.let { route ->
            Routes
                .entries
                .any {
                    it.route.javaClass.kotlin.qualifiedName?.contains(route) == true
                }
        } != false
    val homeUiState by mainViewModel.homeUiState.collectAsState()
    val currentService by mainViewModel.currentService.collectAsState()
    val notifications by mainViewModel.notifications.collectAsState()
    val navigationBarVisible = isParent and (currentService.clientState == ClientState.IDLE)

    Column(modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize().weight(1f),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.Home,
                modifier = Modifier.fillMaxSize().padding(it),
            ) {
                navigation<NavRoutes.Home>(NavRoutes.Map) {
                    composable<NavRoutes.Map> {
                        HomeScreen(
                            homeUiState,
                            currentService,
                            mainViewModel::onAction,
                            onLocationChange,
                            onRequest = onRequest
                        )
                    }
                }
                navigation<NavRoutes.Notifications>(NavRoutes.NotificationsList) {
                    composable<NavRoutes.NotificationsList> {
                        NotificationsScreen(notifications) {
                            navController.navigate(NavRoutes.Notification(it.id))
                        }
                    }
                    composable<NavRoutes.Notification> { args ->
                        val notification = notifications.firstOrNull { it.id == args.id }
                        notification?.let { notification ->
                            NotificationDetails(notification)
                        }
                    }
                }
                composable<NavRoutes.Profile> {
                    ProfileScreen()
                }
                navigation<NavRoutes.Settings>(NavRoutes.SettingsList) {
                    composable<NavRoutes.SettingsList> {
                        SettingsScreen(navController, mainViewModel::onAction)
                    }
                    composable<NavRoutes.ChangePassword> {
                        // ChangePasswordScreen()
                    }
                    composable<NavRoutes.DeleteAccount> {
                        // DeletePasswordScreen()
                    }
                    composable<NavRoutes.CustomizeApp> {
                        CustomizeAppScreen()
                    }
                    composable<NavRoutes.Language> {
                        LanguageScreen()
                    }
                    composable<NavRoutes.About> {
                        AboutScreen()
                    }
                    composable<NavRoutes.TermsOfService> {
                        TermsOfServiceScreen()
                    }
                    composable<NavRoutes.PrivacyPolicy> {
                        PrivacyPolicyScreen()
                    }
                    composable<NavRoutes.Help> {
                        // HelpScreen()
                    }
                }
            }
        }
        AnimatedVisibility(navigationBarVisible, modifier = Modifier.fillMaxWidth()) {
            NavigationBar(navController, currentNavRoute) {
                androidx.compose.animation.AnimatedVisibility(
                    (it == Routes.NOTIFICATIONS) and notifications.isNotEmpty(),
                    enter = materialFadeThroughIn(),
                    exit = materialFadeThroughOut()
                ) {
                    Badge {
                        AnimatedContent(
                            notifications.size,
                            label = "",
                            transitionSpec = intUpDownTransSpec
                        ) {
                            Text("$it")
                        }
                    }
                }
            }
        }
    }
}