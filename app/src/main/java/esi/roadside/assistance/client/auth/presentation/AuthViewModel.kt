package esi.roadside.assistance.client.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.domain.use_case.AuthHome
import esi.roadside.assistance.client.auth.presentation.Action.GoToForgotPassword
import esi.roadside.assistance.client.auth.presentation.Action.GoToLogin
import esi.roadside.assistance.client.auth.presentation.Action.GoToSignup
import esi.roadside.assistance.client.auth.presentation.Action.HideAuthError
import esi.roadside.assistance.client.auth.presentation.Action.Initiate
import esi.roadside.assistance.client.auth.presentation.Action.NextStep
import esi.roadside.assistance.client.auth.presentation.Action.PreviousStep
import esi.roadside.assistance.client.auth.presentation.Action.ShowAuthError
import esi.roadside.assistance.client.auth.presentation.Action.Skip
import esi.roadside.assistance.client.auth.presentation.Action.SkipVerification
import esi.roadside.assistance.client.auth.presentation.screens.AuthUiState
import esi.roadside.assistance.client.auth.presentation.util.loggedIn
import esi.roadside.assistance.client.core.util.account.AccountManager
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.AuthNavigate
import esi.roadside.assistance.client.core.presentation.util.Event.AuthShowError
import esi.roadside.assistance.client.core.presentation.util.Event.LaunchMainActivity
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val accountManager: AccountManager,
    private val authHomeUseCase: AuthHome,
): ViewModel() {
    private val _step = MutableStateFlow(0)
    val step = _step.asStateFlow()

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()


    fun onAction(action: Action) {
        when(action) {
            Initiate -> {
                _authUiState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    accountManager.getUser()?.let {
                        authHomeUseCase()
                            .onSuccess {
                                _authUiState.update {
                                    it.copy(
                                        error = null,
                                        errorDialogVisible = false,
                                        loading = false
                                    )
                                }
                                if (it) loggedIn(accountManager)
                            }.onError { error ->
                                _authUiState.update {
                                    it.copy(
                                        error = error,
                                        errorDialogVisible = error == DomainError.NO_INTERNET,
                                        loading = false,
                                        action = R.string.retry to {
                                            onAction(Initiate)
                                        }
                                    )
                                }
                                println(error)
                            }
                    }
                }
            }
            is GoToLogin -> {
                sendEvent(AuthNavigate(NavRoutes.Login))
            }
            is GoToSignup -> {
                sendEvent(AuthNavigate(NavRoutes.Signup))
            }
            is GoToForgotPassword -> {
                sendEvent(AuthNavigate(NavRoutes.ForgotPassword))
            }
            is ShowAuthError -> {
                sendEvent(AuthShowError(action.error))
            }
            HideAuthError -> {
                _authUiState.update {
                    it.copy(errorDialogVisible = false, error = null)
                }
            }
            SkipVerification -> {
                sendEvent(LaunchMainActivity)
            }

            NextStep -> {
                _step.value++
            }
            PreviousStep -> {
                _step.value--
            }
            Skip -> {
                _step.value = 3
            }
        }
    }

}