package esi.roadside.assistance.client.auth.presentation.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.Action
import esi.roadside.assistance.client.auth.presentation.util.BackgroundBox
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.auth.presentation.util.TermsAndPolicy
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.components.PasswordTextField
import esi.roadside.assistance.client.core.presentation.components.ProfilePicturePicker
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.core.presentation.theme.lightScheme

@Composable
fun SignupScreen(
    uiState: SignupUiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    BackgroundBox(R.drawable.signup_background, modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 48.dp)
                .padding(top = 12.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Join us !",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = lightScheme.background
                )
                Text(
                    text = "Create an account",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = lightScheme.background
                )
                Spacer(Modifier.height(18.dp))
                ProfilePicturePicker(uiState.image, enabled = !uiState.loading) {
                    onAction(Action.SetSignupImage(it))
                }
            }
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyTextField(
                    uiState.fullName,
                    {
                        onAction(Action.SetSignupFullName(it))
                    },
                    label = stringResource(R.string.full_name),
                    placeholder = stringResource(R.string.full_name_placeholder),
                    enabled = !uiState.loading,
                    error = uiState.fullNameError
                )
                MyTextField(
                    uiState.phoneNumber,
                    {
                        onAction(Action.SetSignupPhoneNumber(it))
                    },
                    label = stringResource(R.string.phone_number),
                    placeholder = stringResource(R.string.phone_number_placeholder),
                    error = uiState.phoneNumberError,
                    enabled = !uiState.loading,
                )
                MyTextField(
                    uiState.email,
                    {
                        onAction(Action.SetSignupEmail(it))
                    },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_placeholder),
                    error = uiState.emailError,
                    enabled = !uiState.loading
                )
                PasswordTextField(
                    uiState.password,
                    {
                        onAction(Action.SetSignupPassword(it))
                    },
                    uiState.passwordHidden,
                    {
                        onAction(Action.ToggleSignupPasswordHidden)
                    },
                    error = uiState.passwordError,
                    enabled = !uiState.loading
                )
                PasswordTextField(
                    uiState.confirmPassword,
                    {
                        onAction(Action.SetSignupConfirmPassword(it))
                    },
                    uiState.confirmPasswordHidden,
                    {
                        onAction(Action.ToggleSignupConfirmPasswordHidden)
                    },
                    error = uiState.confirmPasswordError,
                    label = stringResource(R.string.confirm_password),
                    placeholder = stringResource(R.string.confirm_password_placeholder),
                    enabled = !uiState.loading
                )
                Button(
                    stringResource(R.string.continue_text),
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    enabled = !uiState.loading
                ) {
                    onAction(Action.Signup)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.already_have_an_account))
                    TextButton(
                        { onAction(Action.GoToLogin) },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
                        enabled = !uiState.loading,
                    ) {
                        Text(stringResource(R.string.log_in))
                    }
                }
            }
            TermsAndPolicy(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            )
        }
    }
}

@Preview
@Composable
private fun SignupScreenPreview() {
    PreviewAppTheme {
        SignupScreen(SignupUiState(), {})
    }
}