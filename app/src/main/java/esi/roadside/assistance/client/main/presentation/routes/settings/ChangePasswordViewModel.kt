package esi.roadside.assistance.client.main.presentation.routes.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.auth.domain.use_case.ResetPassword
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.core.util.account.AccountManager
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.provider.main.presentation.routes.settings.ChangePasswordAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    accountManager: AccountManager,
    private val resetPassword: ResetPassword
): ViewModel() {
    private val _state = MutableStateFlow(ChangePasswordState())
    val state = _state.asStateFlow()

    private val email = accountManager.getUserFlow().map { it.email }

    init {
        viewModelScope.launch {
            accountManager.getUserFlow().collectLatest {
                Log.i("ChangePasswordViewModel", it.toString())
            }
        }
    }

    fun onAction(action: ChangePasswordAction) {
        when(action) {
            ChangePasswordAction.Confirm -> {
                viewModelScope.launch(Dispatchers.IO) {
                    ValidateInput.validateChangePassword(
//                        _state.value.currentPassword,
                        _state.value.newPassword,
                        _state.value.confirmNewPassword
                    )?.let { error ->
                        _state.update {
                            it.copy(
//                                currentPasswordError = error
//                                    .takeIf { it.field == Field.CURRENT_PASSWORD },
                                newPasswordError = error
                                    .takeIf { it.field == Field.PASSWORD },
                                confirmNewPasswordError = error
                                    .takeIf { it.field == Field.CONFIRM_PASSWORD }
                            )
                        }
                        return@launch
                    }
                    _state.update { it.copy(loading = true) }
                    resetPassword(email.first(), _state.value.newPassword)
                        .onSuccess {
                            sendEvent(Event.ShowMainActivityMessage(R.string.password_reset_success))
                        }.onError { error ->
                            sendEvent(Event.ShowMainActivityMessage(error.text))
                        }
                    _state.update { it.copy(loading = false) }
                }
            }
            is ChangePasswordAction.SetConfirmNewPassword -> {
                _state.update {
                    it.copy(confirmNewPassword = action.value)
                }
            }
            is ChangePasswordAction.SetConfirmNewPasswordVisible -> {
                _state.update {
                    it.copy(confirmNewPasswordVisible = action.value)
                }
            }
//            is ChangePasswordAction.SetCurrentPassword -> {
//                _state.update {
//                    it.copy(currentPassword = action.value)
//                }
//            }
//            is ChangePasswordAction.SetCurrentPasswordVisible -> {
//                _state.update {
//                    it.copy(currentPasswordVisible = action.value)
//                }
//            }
            is ChangePasswordAction.SetNewPassword -> {
                _state.update {
                    it.copy(newPassword = action.value)
                }
            }
            is ChangePasswordAction.SetNewPasswordVisible -> {
                _state.update {
                    it.copy(newPasswordVisible = action.value)
                }
            }
        }
    }
}