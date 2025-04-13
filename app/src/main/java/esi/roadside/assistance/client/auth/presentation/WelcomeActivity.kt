package esi.roadside.assistance.client.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginScreen
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordScreen
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupScreen
import esi.roadside.assistance.client.auth.presentation.screens.signup.VerifyEmailScreen
import esi.roadside.assistance.client.auth.presentation.screens.welcome.WelcomeScreen
import esi.roadside.assistance.client.core.data.networking.constructUrl
import esi.roadside.assistance.client.core.presentation.components.IconDialog
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.main.presentation.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut


class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                SetSystemBarColors()
                val navController = rememberNavController()
                val viewModel: AuthViewModel = koinViewModel()
                val step by viewModel.step.collectAsState()
                val loginUiState by viewModel.loginUiState.collectAsState()
                val signupUiState by viewModel.signupUiState.collectAsState()
                val resetPasswordUiState by viewModel.resetPasswordUiState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                LaunchedEffect(Unit) {
                    viewModel.createAccountManager(this@WelcomeActivity)
                }
                CollectEvents { event ->
                    when (event) {
                        is Event.AuthNavigate -> {
                            navController.navigate(event.route)
                        }
                        is Event.AuthShowError -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(getString(event.error.text))
                            }
                        }
                        Event.LaunchMainActivity -> {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        Event.ImageUploadError -> {
                            Toast.makeText(
                                this,
                                "Error occurred when uploading your image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Event.LaunchGoogleSignIn -> {
                            val url = constructUrl("/auth/google/client")
                            val intent = CustomTabsIntent.Builder().build()
                            intent.launchUrl(this, url.toUri())
                        }
                        else -> Unit
                    }
                }
                AppTheme {
                    Scaffold(
                        Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },
                        contentWindowInsets = WindowInsets(0, 0, 0, 0)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = NavRoutes.Welcome,
                            enterTransition = { materialFadeThroughIn() },
                            exitTransition = { materialFadeThroughOut() },
                            modifier = Modifier.fillMaxSize().padding(it),
                        ) {
                            composable<NavRoutes.Welcome> {
                                WelcomeScreen(step, viewModel::onAction)
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
}