package esi.roadside.assistance.client.auth.presentation.screens.reset_password

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.auth.presentation.util.MyScreen
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme

@Composable
fun ResetPasswordScreen(
    uiState: ResetPasswordUiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    MyScreen(
        if (uiState.sent) stringResource(R.string.enter_reset_code)
        else stringResource(R.string.receive_code),
        if (uiState.sent) stringResource(R.string.enter_the_code_text)
        else stringResource(R.string.send_code_text),
        modifier
    ) {
        Column(
            Modifier.padding(horizontal = 48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedContent(uiState.sent, Modifier.fillMaxWidth(), label = "") {
                if (it)
                    OutlinedTextField(
                        uiState.code,
                        {
                            onAction(Action.SetCode(it))
                        },
                        Modifier.fillMaxWidth(),
                        label = {
                            Text(stringResource(R.string.email))
                        },
                        placeholder = {
                            Text(stringResource(R.string.email_placeholder))
                        },
                        isError = uiState.codeError,
                        supportingText = if (!uiState.codeError) null else {
                            {
                                Text(stringResource(R.string.incorrect_code))
                            }
                        }
                    )
                else
                    OutlinedTextField(
                        uiState.email,
                        {
                            onAction(Action.SetResetPasswordEmail(it))
                        },
                        Modifier.fillMaxWidth(),
                        label = {
                            Text(stringResource(R.string.email))
                        },
                        placeholder = {
                            Text(stringResource(R.string.email_placeholder))
                        },
                        isError = uiState.emailError,
                        supportingText = if (!uiState.emailError) null else {
                            {
                                Text(stringResource(R.string.invalid_email))
                            }
                        }
                    )
            }
            Spacer(Modifier.height(24.dp))
            AnimatedVisibility(uiState.sent) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.didn_t_receive_anything))
                    TextButton(
                        { onAction(Action.Send) },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
                    ) {
                        Text(stringResource(R.string.send_code_again))
                    }
                }
            }
            Button(stringResource(R.string.send_reset_code), Modifier.fillMaxWidth()) {
                onAction(Action.Send)
            }
            Spacer(Modifier.height(40.dp))
            TextButton(
                { onAction(Action.GoToLogin) },
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
        ResetPasswordScreen(ResetPasswordUiState(), {})
    }
}

@Preview
@Composable
private fun ResendScreenPreview() {
    PreviewAppTheme {
        ResetPasswordScreen(ResetPasswordUiState(sent = true), {})
    }
}