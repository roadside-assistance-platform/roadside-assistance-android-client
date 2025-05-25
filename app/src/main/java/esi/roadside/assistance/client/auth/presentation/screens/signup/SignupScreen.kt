package esi.roadside.assistance.client.auth.presentation.screens.signup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.auth.presentation.util.BackgroundBox
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.components.PasswordTextField
import esi.roadside.assistance.client.core.presentation.components.ProfilePicturePicker
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.core.presentation.theme.lightScheme

@Composable
fun SignupScreen(
    viewModel: SignupViewModel,
    modifier: Modifier = Modifier,
    onNavigate: (NavRoutes) -> Unit
) {
    val uiState by viewModel.signupState.collectAsState()
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
                    text = stringResource(R.string.join_us),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    color = lightScheme.background
                )
                Text(
                    text = stringResource(R.string.join_us_signup_text),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = lightScheme.background
                )
                Spacer(Modifier.height(18.dp))
                ProfilePicturePicker(uiState.image, enabled = !uiState.loading) {
                    viewModel.onAction(SignupAction.SetImage(it))
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
                        viewModel.onAction(SignupAction.SetFullName(it))
                    },
                    label = stringResource(R.string.full_name),
                    placeholder = stringResource(R.string.full_name_placeholder),
                    enabled = !uiState.loading,
                    error = uiState.fullNameError,
                    autoCompleteContentType = ContentType.PersonFullName
                )
                MyTextField(
                    uiState.phoneNumber,
                    {
                        viewModel.onAction(SignupAction.SetPhoneNumber(it))
                    },
                    label = stringResource(R.string.phone_number),
                    placeholder = stringResource(R.string.phone_number_placeholder),
                    error = uiState.phoneNumberError,
                    enabled = !uiState.loading,
                    keyboardType = KeyboardType.Phone,
                    autoCompleteContentType = ContentType.PhoneNumberDevice
                )
                MyTextField(
                    uiState.email,
                    {
                        viewModel.onAction(SignupAction.SetEmail(it))
                    },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_placeholder),
                    error = uiState.emailError,
                    enabled = !uiState.loading,
                    keyboardType = KeyboardType.Email,
                    autoCompleteContentType = ContentType.EmailAddress + ContentType.NewUsername + ContentType.Username
                )
                PasswordTextField(
                    uiState.password,
                    {
                        viewModel.onAction(SignupAction.SetPassword(it))
                    },
                    uiState.passwordHidden,
                    {
                        viewModel.onAction(SignupAction.TogglePasswordHidden)
                    },
                    error = uiState.passwordError,
                    enabled = !uiState.loading,
                    autoCompleteContentType = ContentType.NewPassword
                )
                PasswordTextField(
                    uiState.confirmPassword,
                    {
                        viewModel.onAction(SignupAction.SetConfirmPassword(it))
                    },
                    uiState.confirmPasswordHidden,
                    {
                        viewModel.onAction(SignupAction.ToggleConfirmPasswordHidden)
                    },
                    error = uiState.confirmPasswordError,
                    label = stringResource(R.string.confirm_password),
                    placeholder = stringResource(R.string.confirm_password_placeholder),
                    enabled = !uiState.loading,
                    autoCompleteContentType = ContentType.NewPassword
                )
                AnimatedContent(
                    uiState.loading,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    if (it)
                        LinearProgressIndicator(Modifier.padding(vertical = 30.dp).fillMaxWidth())
                    else
                        Button(
                            stringResource(R.string.continue_text),
                            Modifier.fillMaxWidth(),
                            enabled = !uiState.loading
                        ) {
                            viewModel.onAction(SignupAction.Signup)
                        }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.already_have_an_account))
                    TextButton(
                        { onNavigate(NavRoutes.Login) },
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
        }
    }
    BackHandler(uiState.loading) {}
}

@Preview
@Composable
private fun SignupScreenPreview() {
    PreviewAppTheme {
        //SignupScreen(SignupState(), {})
    }
}