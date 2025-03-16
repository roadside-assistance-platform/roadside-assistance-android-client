package esi.roadside.assistance.client.auth.presentation

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.auth.Crypto
import esi.roadside.assistance.client.auth.data.dto.LoginRequest
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.UpdateModel
import esi.roadside.assistance.client.auth.domain.use_case.Cloudinary
import esi.roadside.assistance.client.auth.domain.use_case.GoogleLogin
import esi.roadside.assistance.client.auth.domain.use_case.Login
import esi.roadside.assistance.client.auth.domain.use_case.ResetPassword
import esi.roadside.assistance.client.auth.domain.use_case.SignUp
import esi.roadside.assistance.client.auth.domain.use_case.Update
import esi.roadside.assistance.client.auth.presentation.Action
import esi.roadside.assistance.client.auth.presentation.Action.*
import esi.roadside.assistance.client.auth.presentation.screens.AuthUiState
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginUiState
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordUiState
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupUiState
import esi.roadside.assistance.client.auth.util.account.AccountManager
import esi.roadside.assistance.client.auth.util.account.SignInResult
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.*
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val cloudinaryUseCase: Cloudinary,
    private val loginUseCase: Login,
    private val signUpUseCase: SignUp,
    private val updateUseCase: Update,
    private val resetPasswordUseCase: ResetPassword,
    private val googleLoginUseCase: GoogleLogin,
): ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    private val _signupUiState = MutableStateFlow(SignupUiState())
    val signupUiState = _signupUiState.asStateFlow()

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    private val _resetPasswordUiState = MutableStateFlow(ResetPasswordUiState())
    val resetPasswordUiState = _resetPasswordUiState.asStateFlow()

    private lateinit var accountManager: AccountManager

    fun createAccountManager(activity: Activity) {
        accountManager = AccountManager(activity)
    }

    fun onAction(action: Action) {
        when(action) {
            is Action.GoToLogin -> {
                sendEvent(AuthNavigate(NavRoutes.Login))
                viewModelScope.launch {
                    val result = accountManager.signIn()
                    if (result is SignInResult.Success) {
                        _loginUiState.update {
                            it.copy(email = result.username, password = result.password)
                        }
                    }
                }
            }
            is Action.GoToSignup -> {
                sendEvent(AuthNavigate(NavRoutes.Signup))
            }
            is Action.GoToGoogleLogin -> {
                sendEvent(LaunchGoogleSignIn)
            }
            is Action.GoToForgotPassword -> {
                sendEvent(AuthNavigate(NavRoutes.ForgotPassword))
            }
            is Action.Login -> {
                val inputError = ValidateInput.validateLogin(
                    _loginUiState.value.email,
                    _loginUiState.value.password
                )
                if (inputError != null) {
                    _loginUiState.update {
                        it.copy(
                            emailError = inputError.second.takeIf { inputError.first == Field.EMAIL },
                            passwordError = inputError.second.takeIf { inputError.first == Field.PASSWORD },
                        )
                    }
                    return
                }
                _loginUiState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    loginUseCase(
                        LoginRequestModel(
                            email = _loginUiState.value.email,
                            password = _loginUiState.value.password
                        )
                    ).onSuccess {
                        accountManager.signUp(_loginUiState.value.email, _loginUiState.value.password)
                        loggedIn(it.client)
                    }.onError {
                        println(it)
                        onAction(ShowAuthError(it))
                        _loginUiState.update {
                            it.copy(loading = false)
                        }
                    }
                }
            }
            is Action.Signup -> {
                val inputError = ValidateInput.validateSignup(
                    _signupUiState.value.email,
                    _signupUiState.value.password,
                    _signupUiState.value.confirmPassword,
                    _signupUiState.value.fullName,
                    _signupUiState.value.phoneNumber
                )
                if (inputError != null) {
                    _signupUiState.update {
                        it.copy(
                            emailError = inputError.takeIf { inputError.field == Field.EMAIL },
                            passwordError = inputError.takeIf { inputError.field == Field.PASSWORD },
                            confirmPasswordError = inputError.takeIf { inputError.field == Field.CONFIRM_PASSWORD },
                            fullNameError = inputError.takeIf { inputError.field == Field.FULL_NAME },
                            phoneNumberError = inputError.takeIf { inputError.field == Field.PHONE_NUMBER }
                        )
                    }
                    return
                }
                _signupUiState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    signUpUseCase(
                        SignupModel(
                            email = _signupUiState.value.email,
                            password = _signupUiState.value.password,
                            fullName = _signupUiState.value.fullName,
                            phone = _signupUiState.value.phoneNumber,
                            photo = "_"
                        )
                    ).onSuccess { client ->
                        accountManager.signUp(client.email, _signupUiState.value.password)
                        var url: String? = null
                        println("Signup image is null = ${_signupUiState.value.image == null}")
                        _signupUiState.value.image?.let {
                            cloudinaryUseCase(
                                it,
                                { url = it },
                                { progress ->
                                    _signupUiState.update {
                                        it.copy(uploadProgress = progress)
                                    }
                                },
                                {
                                    sendEvent(ImageUploadError)
                                }
                            )
                        }
                        updateUseCase(
                             UpdateModel(
                                id = client.id,
                                fullName = _signupUiState.value.fullName,
                                phoneNumber = _signupUiState.value.phoneNumber,
                                photo = url
                            )
                        )
                        loggedIn(client, false)
                        sendEvent(AuthNavigate(NavRoutes.VerifyEmail))
                    }.onError {
                        _signupUiState.update {
                            it.copy(loading = false)
                        }
                    }
                }
            }
            is Action.Send -> {
                viewModelScope.launch {
                    resetPasswordUseCase(_resetPasswordUiState.value.email)
                }
            }
            is Action.SetResetPasswordEmail -> {
                _resetPasswordUiState.update {
                    it.copy(email = action.email)
                }
            }
            is Action.SetCode -> {
                _resetPasswordUiState.update {
                    it.copy(code = action.code)
                }
            }
            is Action.SetLoginEmail -> {
                _loginUiState.update {
                    it.copy(email = action.email)
                }
            }
            is Action.SetLoginPassword -> {
                _loginUiState.update {
                    it.copy(password = action.password)
                }
            }

            is Action.SetSignupConfirmPassword -> {
                _signupUiState.update {
                    it.copy(confirmPassword = action.confirmPassword)
                }
            }
            is Action.SetSignupEmail -> {
                _signupUiState.update {
                    it.copy(email = action.email)
                }
            }
            is Action.SetSignupFullName -> {
                _signupUiState.update {
                    it.copy(fullName = action.fullName)
                }
            }
            is Action.SetSignupImage -> {
                _signupUiState.update {
                    it.copy(image = action.image)
                }
            }
            is Action.SetSignupPassword -> {
                _signupUiState.update {
                    it.copy(password = action.password)
                }
            }

            is Action.SetSignupPhoneNumber -> {
                _signupUiState.update {
                    it.copy(phoneNumber = action.phoneNumber)
                }
            }
            is Action.SetVerifyEmailCode -> {
                _signupUiState.update {
                    it.copy(verifyEmailCode = action.code)
                }
            }
            Action.ToggleLoginPasswordHidden -> {
                _loginUiState.update {
                    it.copy(passwordHidden = !it.passwordHidden)
                }
            }
            Action.ToggleSignupConfirmPasswordHidden -> {
                _signupUiState.update {
                    it.copy(confirmPasswordHidden = !it.confirmPasswordHidden)
                }
            }
            Action.ToggleSignupPasswordHidden -> {
                _signupUiState.update {
                    it.copy(passwordHidden = !it.passwordHidden)
                }
            }
            is Action.ShowAuthError -> {
                _authUiState.update {
                    it.copy(errorDialogVisible = true, error = action.error)
                }
            }
            Action.HideAuthError -> {
                _authUiState.update {
                    it.copy(errorDialogVisible = false, error = null)
                }
            }
            Action.SkipVerification -> {
                sendEvent(LaunchMainActivity)
            }
            is Action.GoogleLogin -> {
                viewModelScope.launch {
                    googleLoginUseCase(action.result)
                        .onSuccess {
                            loggedIn(it)
                        }.onError {
                            onAction(ShowAuthError(it))
                        }
                }
            }
        }
    }

    private fun loggedIn(client: ClientModel, launchMainActivity: Boolean = true) {
        
        if (launchMainActivity) sendEvent(LaunchMainActivity)
    }
}