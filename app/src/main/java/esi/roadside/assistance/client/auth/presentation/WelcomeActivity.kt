package esi.roadside.assistance.client.auth.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginScreen
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordScreen
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupScreen
import esi.roadside.assistance.client.auth.presentation.screens.signup.VerifyEmailScreen
import esi.roadside.assistance.client.auth.presentation.screens.welcome.GetStartedScreen
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.EventBus
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.main.presentation.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import kotlin.getValue

class WelcomeActivity : ComponentActivity() {
    val settingsDataStore by inject<SettingsDataStore>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetSystemBarColors(settingsDataStore)
            val navController = rememberNavController()
            val viewModel = getViewModel<AuthViewModel>()
            val loginUiState by viewModel.loginUiState.collectAsState()
            val signupUiState by viewModel.signupUiState.collectAsState()
            val resetPasswordUiState by viewModel.resetPasswordUiState.collectAsState()
            CollectEvents { event ->
                when (event) {
                    is Event.AuthNavigate -> {
                        navController.navigate(event.route)
                    }
                    Event.LaunchMainActivity -> {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
            AppTheme {
                Surface(Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Welcome,
                        enterTransition = { materialFadeThroughIn() },
                        exitTransition = { materialFadeThroughOut() },
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        composable<NavRoutes.Welcome> {
                            GetStartedScreen(viewModel::onAction)
                        }
                        composable<NavRoutes.Login> {
                            LoginScreen(loginUiState, viewModel::onAction)
                        }
                        composable<NavRoutes.Signup> {
                            SignupScreen(signupUiState, viewModel::onAction)
                        }
                        composable<NavRoutes.VerifyEmail> {
                            VerifyEmailScreen(signupUiState, viewModel::onAction)
                        }
                        composable<NavRoutes.ForgotPassword> {
                            ResetPasswordScreen(resetPasswordUiState, viewModel::onAction)
                        }
                    }
                }
            }
        }
    }
}