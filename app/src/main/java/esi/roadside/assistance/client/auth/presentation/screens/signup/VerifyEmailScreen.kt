package esi.roadside.assistance.client.auth.presentation.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginUiState
import esi.roadside.assistance.client.auth.presentation.util.MyScreen
import esi.roadside.assistance.client.auth.presentation.util.TermsAndPolicy
import esi.roadside.assistance.client.core.presentation.components.MyTextField
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme

@Composable
fun VerifyEmailScreen(
    uiState: SignupUiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    MyScreen(
        stringResource(R.string.verify_email),
        stringResource(R.string.verify_email_text),
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
                uiState.verifyEmailCode,
                {
                    onAction(Action.SetVerifyEmailCode(it))
                },
                label = stringResource(R.string.verification_code),
                placeholder = stringResource(R.string.enter_the_code),
                isError = uiState.verifyEmailCodeError,
                supportingText = stringResource(R.string.incorrect_code).takeIf { uiState.verifyEmailCodeError }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.didn_t_receive_anything))
                TextButton(
                    { onAction(Action.GoToSignup) },
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Text(stringResource(R.string.send_code_again))
                }
            }
            Button({ onAction(Action.Signup) }, Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.sign_up))
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
private fun VerifyEmailScreenPreview() {
    PreviewAppTheme {
        VerifyEmailScreen(SignupUiState(), {})
    }
}