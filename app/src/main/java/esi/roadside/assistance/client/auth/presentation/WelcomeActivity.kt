@file:Suppress("DEPRECATION")

package esi.roadside.assistance.client.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginScreen
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordScreen
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupScreen
import esi.roadside.assistance.client.auth.presentation.screens.signup.VerifyEmailScreen
import esi.roadside.assistance.client.auth.presentation.screens.welcome.WelcomeScreen
import esi.roadside.assistance.client.core.presentation.components.IconDialog
import esi.roadside.assistance.client.core.presentation.theme.AppTheme
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.util.composables.CollectEvents
import esi.roadside.assistance.client.core.util.composables.SetSystemBarColors
import esi.roadside.assistance.client.main.presentation.MainActivity
import org.koin.androidx.compose.koinViewModel
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

class WelcomeActivity : ComponentActivity() {
//    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(BuildConfig.WEB_CLIENT_ID)
//        .requestEmail()
//        .build()
//    val googleSignInClient = GoogleSignIn.getClient(this, gso)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetSystemBarColors()
            val navController = rememberNavController()
            val viewModel: AuthViewModel = koinViewModel()
            val loginUiState by viewModel.loginUiState.collectAsState()
            val signupUiState by viewModel.signupUiState.collectAsState()
            val authUiState by viewModel.authUiState.collectAsState()
            val resetPasswordUiState by viewModel.resetPasswordUiState.collectAsState()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    account.idToken?.let { viewModel.onAction(Action.GoogleOldLogin(it)) }
                } catch (_: ApiException) {

                }
            }
            LaunchedEffect(Unit) {
                viewModel.createAccountManager(this@WelcomeActivity)
            }
            CollectEvents { event ->
                when (event) {
                    is Event.AuthNavigate -> {
                        navController.navigate(event.route)
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
//                        launcher.launch(googleSignInClient.signInIntent)
                    }
                    else -> Unit
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
                            WelcomeScreen(viewModel::onAction)
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
                    IconDialog(
                        authUiState.errorDialogVisible,
                        { viewModel.onAction(Action.HideAuthError) },
                        Icons.Outlined.ErrorOutline,
                        stringResource(R.string.error),
                        authUiState.error?.text?.let { stringResource(it) } ?: "",
                    )
                }
            }
        }
    }
}