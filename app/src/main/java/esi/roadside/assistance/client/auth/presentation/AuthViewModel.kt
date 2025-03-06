package esi.roadside.assistance.client.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import esi.roadside.assistance.client.auth.domain.models.SignupRequest
import esi.roadside.assistance.client.auth.domain.models.UpdateRequest
import esi.roadside.assistance.client.auth.domain.use_case.Cloudinary
import esi.roadside.assistance.client.auth.domain.use_case.GoogleLogin
import esi.roadside.assistance.client.auth.domain.use_case.Login
import esi.roadside.assistance.client.auth.domain.use_case.ResetPassword
import esi.roadside.assistance.client.auth.domain.use_case.SignUp
import esi.roadside.assistance.client.auth.domain.use_case.Update
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginUiState
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordUiState
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupUiState
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.*
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

    private val _resetPasswordUiState = MutableStateFlow(ResetPasswordUiState())
    val resetPasswordUiState = _resetPasswordUiState.asStateFlow()

    fun onAction(action: Action) {
        when(action) {
            is Action.GoToLogin -> {
                sendEvent(AuthNavigate(NavRoutes.Login))
            }
            is Action.GoToSignup -> {
                sendEvent(AuthNavigate(NavRoutes.Signup))
            }
            is Action.GoToGoogleLogin -> {
                viewModelScope.launch {
                    googleLoginUseCase()
                }
            }
            is Action.GoToForgotPassword -> {
                sendEvent(AuthNavigate(NavRoutes.ForgotPassword))
            }
            is Action.Login -> {
//                viewModelScope.launch {
//                    loginUseCase(LoginRequest(
//                        email = _loginUiState.value.email,
//                        password = _loginUiState.value.password
//                    ))
//                }
                sendEvent(LaunchMainActivity)
            }
            is Action.Signup -> {
                viewModelScope.launch {
                    signUpUseCase(
                        SignupRequest(
                            username = _signupUiState.value.email,
                            password = _signupUiState.value.password
                        )
                    ).onSuccess { client ->
                        var url: String? = null
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
                            UpdateRequest(
                                id = client.id,
                                fullName = _signupUiState.value.fullName,
                                phoneNumber = _signupUiState.value.phoneNumber,
                                photo = url
                            )
                        )
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
        }
    }
}