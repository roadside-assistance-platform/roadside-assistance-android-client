package esi.roadside.assistance.client.auth.presentation.screens.signup

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.Action
import esi.roadside.assistance.client.auth.presentation.OtpAction
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.auth.presentation.util.MyScreen
import esi.roadside.assistance.client.core.presentation.theme.PreviewAppTheme

@Composable
fun VerifyEmailScreen(
    state: OtpState,
    loading: Boolean,
    onAction: (OtpAction) -> Unit,
    onResend: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequesters = remember {
        List(6) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(loading) {
        if (!loading)
            focusRequesters[state.code.indexOfFirst { it == null }.takeUnless { it == -1 } ?: 5]
                .requestFocus()
    }

    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if(allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }
    MyScreen(
        stringResource(R.string.verify_email),
        stringResource(R.string.verify_email_text),
        modifier
    ) {
        Column(
            Modifier.padding(horizontal = 48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OtpScreen(
                state = state,
                focusRequesters = focusRequesters,
                enabled = !loading,
                onAction = { action ->
                    when(action) {
                        is OtpAction.OnEnterNumber -> {
                            if(action.number != null) {
                                focusRequesters[action.index].freeFocus()
                            }
                        }
                        else -> Unit
                    }
                    onAction(action)
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.didn_t_receive_anything))
                TextButton(
                    onResend,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                ) {
                    Text(stringResource(R.string.send_code_again))
                }
            }
            AnimatedContent(loading) {
                if (it)
                    LinearProgressIndicator(Modifier.padding(vertical = 30.dp).fillMaxWidth())
                else
                    Button(
                        stringResource(R.string.sign_up),
                        Modifier.fillMaxWidth(),
                        enabled = !loading,
                        onClick = onConfirm
                    )
            }
        }
    }
    BackHandler(loading) {}
}

@Preview
@Composable
private fun VerifyEmailScreenPreview() {
    PreviewAppTheme {
    }
}