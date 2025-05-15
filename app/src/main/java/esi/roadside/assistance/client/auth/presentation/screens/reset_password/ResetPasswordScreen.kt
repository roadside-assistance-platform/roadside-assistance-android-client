package esi.roadside.assistance.client.auth.presentation.screens.reset_password

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.auth.presentation.OtpAction
import esi.roadside.assistance.client.auth.presentation.screens.signup.OtpScreen
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.auth.presentation.util.MyScreen
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.components.PasswordTextField
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavRoutes) -> Unit
) {
    val viewModel: ResetPasswordViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()
    val otpState by viewModel.otpState.collectAsState()
    val focusRequesters = remember {
        List(6) { FocusRequester() }
    }
    MyScreen(
        title = stringResource(uiState.userState.title),
        text = stringResource(uiState.userState.text),
        modifier
    ) {
        Column(
            Modifier.padding(horizontal = 48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedContent(uiState.userState, Modifier.fillMaxWidth(), label = "") {
                when (it) {
                    ResetPasswordUserState.EnterEmail -> {
                        MyTextField(
                            uiState.email,
                            {
                                viewModel.onAction(ResetPasswordAction.SetEmail(it))
                            },
                            Modifier.fillMaxWidth(),
                            label = stringResource(R.string.email),
                            placeholder = stringResource(R.string.email_placeholder),
                            error = uiState.emailError,
                            enabled = !uiState.loading
                        )
                    }
                    ResetPasswordUserState.EnterCode -> {
                        OtpScreen(
                            state = otpState,
                            focusRequesters = focusRequesters,
                            enabled = !uiState.loading,
                            onAction = { action ->
                                if (action is OtpAction.OnEnterNumber)
                                    if (action.number != null) {
                                        focusRequesters[action.index].freeFocus()
                                    }
                                viewModel.onOtpAction(action)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    }
                    ResetPasswordUserState.ResetPassword -> {
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PasswordTextField(
                                uiState.password,
                                {
                                    viewModel.onAction(ResetPasswordAction.SetPassword(it))
                                },
                                uiState.passwordHidden,
                                {
                                    viewModel.onAction(ResetPasswordAction.SetPasswordHidden(it))
                                },
                                error = uiState.passwordError,
                                enabled = !uiState.loading
                            )
                            PasswordTextField(
                                uiState.confirmPassword,
                                {
                                    viewModel.onAction(ResetPasswordAction.SetConfirmPassword(it))
                                },
                                uiState.confirmPasswordHidden,
                                {
                                    viewModel.onAction(ResetPasswordAction.SetConfirmPasswordHidden(it))
                                },
                                label = stringResource(R.string.confirm_password),
                                placeholder = stringResource(R.string.confirm_password_placeholder),
                                error = uiState.confirmPasswordError,
                                enabled = !uiState.loading
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            AnimatedVisibility(uiState.userState == ResetPasswordUserState.EnterCode) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.didn_t_receive_anything))
                    TextButton(
                        { viewModel.onAction(ResetPasswordAction.Send) },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
                    ) {
                        Text(stringResource(R.string.send_code_again))
                    }
                }
            }
            AnimatedContent(uiState.loading) {
                if (it)
                    LinearProgressIndicator(Modifier.padding(vertical = 30.dp).fillMaxWidth())
                else
                    Button(
                        stringResource(uiState.userState.buttonText),
                        Modifier.fillMaxWidth()
                    ) {
                        viewModel.onAction(
                            when(uiState.userState) {
                                ResetPasswordUserState.EnterEmail -> ResetPasswordAction.Send
                                ResetPasswordUserState.EnterCode -> ResetPasswordAction.Verify
                                ResetPasswordUserState.ResetPassword -> ResetPasswordAction.ResetPassword
                            }
                        )
                    }
            }
            Spacer(Modifier.height(40.dp))
            TextButton(
                { onNavigate(NavRoutes.Login) },
                contentPadding = PaddingValues(horizontal = 8.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
            ) {
                Text(stringResource(R.string.back_to_login))
            }
        }
    }
}

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    PreviewAppTheme {
        //ResetPasswordScreen(ResetPasswordState(), {})
    }
}

@Preview
@Composable
private fun ResendScreenPreview() {
    PreviewAppTheme {
        //ResetPasswordScreen(ResetPasswordState(sent = true), {})
    }
}