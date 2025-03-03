package esi.roadside.assistance.client.auth.presentation.screens.signup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.Action
import esi.roadside.assistance.client.auth.presentation.util.BackgroundBox
import esi.roadside.assistance.client.auth.presentation.util.TermsAndPolicy
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.components.PasswordTextField
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme
import esi.roadside.assistance.client.core.presentation.theme.lightScheme

@Composable
fun SignupScreen(
    uiState: SignupUiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                it?.let { onAction(Action.SetSignupImage(it)) }
            },
        )
    BackgroundBox(R.drawable.signup_background, modifier) {
        Column(
            Modifier
                .fillMaxSize()
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
                Box(
                    Modifier
                        .size(140.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        null,
                        Modifier.size(48.dp),
                        MaterialTheme.colorScheme.onSurface,
                    )
                    uiState.image?.let {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        SmallFloatingActionButton(
                            { onAction(Action.SetSignupImage(null)) },
                            Modifier
                                .align(Alignment.BottomEnd)
                                .offset((-8).dp, (-8).dp)
                        ) {
                            Icon(Icons.Default.Delete, null)
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
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
                )
                MyTextField(
                    uiState.phoneNumber,
                    {
                        onAction(Action.SetSignupPhoneNumber(it))
                    },
                    label = stringResource(R.string.phone_number),
                    placeholder = stringResource(R.string.phone_number_placeholder),
                    isError = uiState.phoneNumberError,
                    supportingText = stringResource(R.string.invalid_phone_number).takeIf { uiState.phoneNumberError }
                )
                MyTextField(
                    uiState.email,
                    {
                        onAction(Action.SetSignupEmail(it))
                    },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_placeholder),
                    isError = uiState.emailError,
                    supportingText = stringResource(R.string.invalid_email).takeIf { uiState.emailError }
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
                    isError = uiState.passwordError,
                    supportingText = stringResource(R.string.incorrect_password).takeIf { uiState.passwordError }
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
                    isError = uiState.confirmPasswordError,
                    label = stringResource(R.string.confirm_password),
                    placeholder = stringResource(R.string.confirm_password_placeholder),
                    supportingText = stringResource(R.string.incorrect_password).takeIf { uiState.confirmPasswordError }
                )
                Button({ onAction(Action.Signup) }, Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Text(stringResource(R.string.continue_text))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.already_have_an_account))
                    TextButton(
                        { onAction(Action.GoToLogin) },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
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
        SignupScreen(SignupUiState("", ""), {})
    }
}