package esi.roadside.assistance.client.auth.presentation.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.Action
import esi.roadside.assistance.client.auth.presentation.util.MyScreen
import esi.roadside.assistance.client.auth.presentation.util.TermsAndPolicy
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.components.PasswordTextField
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    MyScreen(
        stringResource(R.string.welcome_back),
        stringResource(R.string.log_in_to_continue),
        modifier
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MyTextField(
                uiState.email,
                {
                    onAction(Action.SetLoginEmail(it))
                },
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.email_placeholder),
                isError = uiState.emailError,
                supportingText = stringResource(R.string.invalid_email).takeIf { uiState.emailError },
            )
            PasswordTextField(
                uiState.password,
                {
                    onAction(Action.SetLoginPassword(it))
                },
                uiState.passwordHidden,
                {
                    onAction(Action.ToggleLoginPasswordHidden)
                },
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password_placeholder),
                supportingText = stringResource(R.string.incorrect_password).takeIf { uiState.passwordError },
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton({ onAction(Action.GoToForgotPassword) }) {
                    Text(stringResource(R.string.forgot_password))
                }
            }
            Button({ onAction(Action.Login) }, Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.log_in))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.don_t_have_an_account))
                TextButton(
                    { onAction(Action.GoToSignup) },
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Text(stringResource(R.string.sign_up))
                }
            }
        }
        TermsAndPolicy(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max))
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    PreviewAppTheme {
        LoginScreen(LoginUiState("", ""), {})
    }
}