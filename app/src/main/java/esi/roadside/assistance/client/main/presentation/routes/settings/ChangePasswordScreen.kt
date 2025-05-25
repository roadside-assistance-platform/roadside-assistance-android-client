package esi.roadside.assistance.client.main.presentation.routes.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.util.Button
import esi.roadside.assistance.client.core.presentation.components.PasswordTextField
import esi.roadside.assistance.client.main.presentation.routes.settings.ChangePasswordAction
import esi.roadside.assistance.client.main.util.plus
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<ChangePasswordViewModel>()
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier =
        modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.change_password),
                background = R.drawable.union,
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets.ime.add(WindowInsets(bottom = 16.dp)),
    ) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = it + PaddingValues(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            item {
//                PasswordTextField(
//                    value = state.currentPassword,
//                    onValueChange = {
//                        viewModel.onAction(ChangePasswordAction.SetCurrentPassword(it))
//                    },
//                    passwordHidden = state.currentPasswordVisible,
//                    placeholder = stringResource(R.string.current_password),
//                    label = stringResource(R.string.current_password),
//                    onPasswordHiddenChange = {
//                        viewModel.onAction(ChangePasswordAction.SetCurrentPasswordVisible(it))
//                    },
//                    error = state.currentPasswordError,
//                    modifier = Modifier.fillMaxWidth(),
//                    autoCompleteContentType = ContentType.Password
//                )
//            }
            item {
                PasswordTextField(
                    value = state.newPassword,
                    onValueChange = {
                        viewModel.onAction(ChangePasswordAction.SetNewPassword(it))
                    },
                    passwordHidden = state.newPasswordVisible,
                    placeholder = stringResource(R.string.new_password),
                    label = stringResource(R.string.new_password),
                    onPasswordHiddenChange = {
                        viewModel.onAction(ChangePasswordAction.SetNewPasswordVisible(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    error = state.newPasswordError,
                    autoCompleteContentType = ContentType.NewPassword
                )
            }
            item {
                PasswordTextField(
                    value = state.confirmNewPassword,
                    onValueChange = {
                        viewModel.onAction(ChangePasswordAction.SetConfirmNewPassword(it))
                    },
                    passwordHidden = state.confirmNewPasswordVisible,
                    placeholder = stringResource(R.string.confirm_new_password),
                    label = stringResource(R.string.confirm_new_password),
                    onPasswordHiddenChange = {
                        viewModel.onAction(ChangePasswordAction.SetConfirmNewPasswordVisible(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    error = state.confirmNewPasswordError,
                    autoCompleteContentType = ContentType.NewPassword
                )
            }
            item {
                AnimatedContent(state.loading, Modifier.padding(top = 16.dp)) {
                    if (it)
                        LinearProgressIndicator(Modifier.fillMaxWidth(.5f).padding(vertical = 30.dp))
                    else
                        Button(stringResource(R.string.ok), Modifier.fillMaxWidth(.5f)) {
                            viewModel.onAction(ChangePasswordAction.Confirm)
                        }
                }
            }
        }
    }
}