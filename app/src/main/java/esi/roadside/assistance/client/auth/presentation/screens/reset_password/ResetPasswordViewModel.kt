package esi.roadside.assistance.client.auth.presentation.screens.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.domain.models.SendEmailModel
import esi.roadside.assistance.client.auth.domain.models.VerifyEmailModel
import esi.roadside.assistance.client.auth.domain.use_case.ResetPassword
import esi.roadside.assistance.client.auth.domain.use_case.SendEmail
import esi.roadside.assistance.client.auth.domain.use_case.VerifyEmail
import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.auth.presentation.OtpAction
import esi.roadside.assistance.client.auth.presentation.OtpState
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.AuthNavigate
import esi.roadside.assistance.client.core.presentation.util.Event.AuthShowError
import esi.roadside.assistance.client.core.presentation.util.Event.ShowAuthActivityMessage
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val sendEmailUseCase: SendEmail,
    private val verifyEmail: VerifyEmail,
    private val resetPasswordUseCase: ResetPassword,
): ViewModel() {
    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state.asStateFlow()

    private val _otpState = MutableStateFlow(OtpState())
    val otpState = _otpState.asStateFlow()

    fun onOtpAction(action: OtpAction) {
        _otpState.update {
            if ((action is OtpAction.OnEnterNumber) and (it.isCodeComplete()))
                onAction(ResetPasswordAction.Verify)
            it.onOtpAction(action)
        }
    }

    fun onAction(action: ResetPasswordAction) {
        when(action) {
            is ResetPasswordAction.Send -> {
                ValidateInput.validateEmail(_state.value.email)?.let { error ->
                    _state.update {
                        it.copy(emailError = error.takeIf { it.field == Field.EMAIL })
                    }
                    return
                }
                _state.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    sendEmailUseCase(SendEmailModel(_state.value.email))
                        .onSuccess {
                            _state.update {
                                it.copy(userState = ResetPasswordUserState.EnterCode)
                            }
                        }
                        .onError {
                            sendEvent(AuthShowError(it))
                        }
                    _state.update {
                        it.copy(loading = false)
                    }
                }
            }
            is ResetPasswordAction.SetEmail -> {
                _state.update {
                    it.copy(email = action.email)
                }
            }
            ResetPasswordAction.ResetPassword -> {
                ValidateInput.validateResetPassword(
                    _state.value.password,
                    _state.value.confirmPassword
                )?.let { error ->
                    _state.update {
                        it.copy(
                            passwordError =
                                error.takeIf { it.field == Field.PASSWORD },
                            confirmPasswordError =
                                error.takeIf { it.field == Field.CONFIRM_PASSWORD }
                        )
                    }
                    return
                }
                _state.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    resetPasswordUseCase(
                        _state.value.email,
                        _state.value.password
                    ).onSuccess {
                        sendEvent(ShowAuthActivityMessage(R.string.password_reset_success))
                        sendEvent(AuthNavigate(NavRoutes.Login))
                    }.onError {
                        sendEvent(AuthShowError(it))
                    }
                    _state.update {
                        it.copy(loading = false)
                    }
                }
            }
            ResetPasswordAction.Verify -> {
                _state.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    verifyEmail(VerifyEmailModel(_state.value.email, _otpState.value.getCode()))
                        .onSuccess {
                            _state.update {
                                it.copy(userState = ResetPasswordUserState.ResetPassword)
                            }
                        }
                        .onError {
                            sendEvent(AuthShowError(it))
                        }
                    _state.update {
                        it.copy(loading = false)
                    }
                }
            }
            is ResetPasswordAction.SetConfirmPassword -> {
                _state.update {
                    it.copy(confirmPassword = action.confirmPassword)
                }
            }
            is ResetPasswordAction.SetConfirmPasswordHidden -> {
                _state.update {
                    it.copy(confirmPasswordHidden = action.hidden)
                }
            }
            is ResetPasswordAction.SetPassword -> {
                _state.update {
                    it.copy(password = action.password)
                }
            }
            is ResetPasswordAction.SetPasswordHidden -> {
                _state.update {
                    it.copy(passwordHidden = action.hidden)
                }
            }
        }
    }
}