package esi.roadside.assistance.client.auth.presentation.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.auth.presentation.util.MyScreen
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.components.PasswordTextField
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.settings.util.findActivity
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavRoutes) -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.loginUiState.collectAsState()
    val context = LocalContext.current
    MyScreen(
        stringResource(R.string.welcome_back),
        stringResource(R.string.log_in_to_continue),
        modifier
    ) {
        Column(
            Modifier.padding(horizontal = 48.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MyTextField(
                uiState.email,
                {
                    viewModel.onAction(LoginAction.SetEmail(it))
                },
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.email_placeholder),
                enabled = !uiState.loading,
                error = uiState.emailError,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                autoCompleteContentType = ContentType.Username
            )
            PasswordTextField(
                uiState.password,
                {
                    viewModel.onAction(LoginAction.SetPassword(it))
                },
                uiState.passwordHidden,
                {
                    viewModel.onAction(LoginAction.TogglePasswordHidden)
                },
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password_placeholder),
                enabled = !uiState.loading,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
                error = uiState.passwordError,
                autoCompleteContentType = ContentType.Password
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton({ onNavigate(NavRoutes.ForgotPassword) }, enabled = !uiState.loading) {
                    Text(stringResource(R.string.forgot_password))
                }
            }
            AnimatedContent(uiState.loading) {
                if (it)
                    LinearProgressIndicator(Modifier.padding(vertical = 30.dp).fillMaxWidth())
                else
                    Button(stringResource(R.string.log_in), Modifier.fillMaxWidth(), enabled = !uiState.loading) {
                        viewModel.onAction(LoginAction.Login)
                    }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.don_t_have_an_account))
                TextButton(
                    { onNavigate(NavRoutes.Signup) },
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                    enabled = !uiState.loading,
                ) {
                    Text(stringResource(R.string.sign_up))
                }
            }
        }
    }
    BackHandler(uiState.loading) {}
}

@PreviewScreenSizes
@Composable
private fun LoginScreenPreview() {
    PreviewAppTheme {
        //LoginScreen(LoginState("", ""), {}, {})
    }
}