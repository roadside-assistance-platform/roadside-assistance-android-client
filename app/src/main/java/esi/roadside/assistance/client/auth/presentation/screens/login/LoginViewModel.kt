package esi.roadside.assistance.client.auth.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.use_case.Login
import esi.roadside.assistance.client.auth.presentation.util.loggedIn
import esi.roadside.assistance.client.core.util.account.AccountManager
import esi.roadside.assistance.client.core.util.account.SignInResult
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val accountManager: AccountManager,
    private val loginUseCase: Login,
): ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginUiState = _loginState.asStateFlow()

    fun onAction(action: LoginAction) {
        when(action) {
            LoginAction.Autofill -> {
                viewModelScope.launch {
                    val result = accountManager.signIn()
                    if (result is SignInResult.Success) {
                        _loginState.update {
                            it.copy(email = result.username, password = result.password)
                        }
                    }
                }
            }
            LoginAction.Login -> {
                val inputError = ValidateInput.validateLogin(_loginState.value.email, _loginState.value.password)
                if (inputError != null) {
                    _loginState.update {
                        it.copy(
                            emailError = inputError.second.takeIf { inputError.first == Field.EMAIL },
                            passwordError = inputError.second.takeIf { inputError.first == Field.PASSWORD },
                        )
                    }
                    return
                }
                _loginState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    loginUseCase(
                        LoginRequestModel(
                            email = _loginState.value.email,
                            password = _loginState.value.password
                        )
                    ).onSuccess {
                        loggedIn(accountManager, it.user)
                    }.onError {
                        sendEvent(Event.AuthShowError(it))
                        _loginState.update {
                            it.copy(loading = false)
                        }
                    }
                }
            }
            is LoginAction.SetEmail -> {
                _loginState.update {
                    it.copy(email = action.email)
                }
            }
            is LoginAction.SetPassword -> {
                _loginState.update {
                    it.copy(password = action.password)
                }
            }
            LoginAction.TogglePasswordHidden -> {
                _loginState.update {
                    it.copy(passwordHidden = !it.passwordHidden)
                }
            }
        }
    }
}