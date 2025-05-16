package esi.roadside.assistance.client.auth.presentation.screens.signup

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.domain.models.SendEmailModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.VerifyEmailModel
import esi.roadside.assistance.client.auth.domain.use_case.Cloudinary
import esi.roadside.assistance.client.auth.domain.use_case.SendEmail
import esi.roadside.assistance.client.auth.domain.use_case.SignUp
import esi.roadside.assistance.client.auth.domain.use_case.VerifyEmail
import esi.roadside.assistance.client.auth.presentation.NavRoutes
import esi.roadside.assistance.client.auth.presentation.OtpAction
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordAction
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupAction.*
import esi.roadside.assistance.client.auth.presentation.util.loggedIn
import esi.roadside.assistance.client.auth.util.account.AccountManager
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event.*
import esi.roadside.assistance.client.core.presentation.util.Event.AuthNavigate
import esi.roadside.assistance.client.core.presentation.util.Event.AuthShowError
import esi.roadside.assistance.client.core.presentation.util.Event.ImageUploadError
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.filterNotNull
import kotlin.collections.getOrNull
import kotlin.collections.mapIndexed
import kotlin.collections.none


class SignupViewModel(
    private val accountManager: AccountManager,
    private val cloudinaryUseCase: Cloudinary,
    private val sendEmailUseCase: SendEmail,
    private val signUpUseCase: SignUp,
    private val verifyEmailUseCase: VerifyEmail,
): ViewModel() {
    private val _signupState = MutableStateFlow(SignupState())
    val signupState = _signupState.asStateFlow()

    private val _otpUiState = MutableStateFlow(OtpState())
    val otpUiState = _otpUiState.asStateFlow()

    fun onOtpAction(action: OtpAction) {
        _otpUiState.update {
            if ((action is OtpAction.OnEnterNumber) and (it.isCodeComplete()))
                onAction(Verify)
            it.onOtpAction(action)
        }
    }

    fun onAction(action: SignupAction) {
        when(action) {
            is SetConfirmPassword -> {
                _signupState.update {
                    it.copy(confirmPassword = action.confirmPassword)
                }
            }
            is SetEmail -> {
                _signupState.update {
                    it.copy(email = action.email)
                }
            }
            is SetFullName -> {
                _signupState.update {
                    it.copy(fullName = action.fullName)
                }
            }
            is SetImage -> {
                _signupState.update {
                    it.copy(image = action.image)
                }
            }
            is SetPassword -> {
                _signupState.update {
                    it.copy(password = action.password)
                }
            }

            is SetPhoneNumber -> {
                _signupState.update {
                    it.copy(phoneNumber = action.phoneNumber)
                }
            }
            is SetVerifyEmailCode -> {
                _signupState.update {
                    it.copy(verifyEmailCode = action.code)
                }
            }
            ToggleConfirmPasswordHidden -> {
                _signupState.update {
                    it.copy(confirmPasswordHidden = !it.confirmPasswordHidden)
                }
            }
            TogglePasswordHidden -> {
                _signupState.update {
                    it.copy(passwordHidden = !it.passwordHidden)
                }
            }
            is Signup -> {
                val inputError = ValidateInput.validateSignup(
                    _signupState.value.email,
                    _signupState.value.password,
                    _signupState.value.confirmPassword,
                    _signupState.value.fullName,
                    _signupState.value.phoneNumber
                )
                if (inputError != null) {
                    _signupState.update {
                        it.copy(
                            emailError = inputError.takeIf { inputError.field == Field.EMAIL },
                            passwordError = inputError.takeIf { inputError.field == Field.PASSWORD },
                            confirmPasswordError = inputError.takeIf { inputError.field == Field.CONFIRM_PASSWORD },
                            fullNameError = inputError.takeIf { inputError.field == Field.FULL_NAME },
                            phoneNumberError = inputError.takeIf { inputError.field == Field.PHONE_NUMBER }
                        )
                    }
                } else {
                    _signupState.update {
                        it.copy(loading = true)
                    }
                    viewModelScope.launch {
                        var url: String? = null
                        cloudinaryUseCase(
                            image = _signupState.value.image ?: "".toUri(),
                            onSuccess = {
                                url = it
                            },
                            onProgress = { progress ->
                                _signupState.update {
                                    it.copy(uploadProgress = progress)
                                }
                            },
                            onFailure = {
                                sendEvent(ImageUploadError)
                            },
                            onFinished = {
                                _signupState.update {
                                    it.copy(photo = url ?: "_", loading = false)
                                }
                                //onAction(SendCode(_signupState.value.email))
                            }
                        )
                    }
                }
            }
            is Verify -> {
                _signupState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    verifyEmailUseCase(
                        VerifyEmailModel(
                            _signupState.value.email,
                            _otpUiState.value.code.filterNotNull().joinToString(""),
                        )
                    ).onSuccess {
                        signUpUseCase(
                            SignupModel(
                                email = _signupState.value.email,
                                password = _signupState.value.password,
                                fullName = _signupState.value.fullName,
                                phone = _signupState.value.phoneNumber,
                                photo = _signupState.value.photo,
                            )
                        ).onSuccess { response ->
                            accountManager.signUp(
                                _signupState.value.email,
                                _signupState.value.password
                            )
                            _signupState.update { it.copy(loading = false) }
                            loggedIn(accountManager, response.user)
                        }.onError {
                            _signupState.update {
                                it.copy(loading = false)
                            }
                            sendEvent(AuthShowError(it))
                        }
                    }.onError {
                        _signupState.update {
                            it.copy(loading = false)
                        }
                        sendEvent(AuthShowError(it))
                    }
                }
            }
            is SendCode -> {
                _signupState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    sendEmailUseCase(SendEmailModel(action.email))
                        .onSuccess {
                            _signupState.update {
                                it.copy(loading = false)
                            }
                            _otpUiState.value = OtpState()
                            sendEvent(ShowAuthActivityMessage(R.string.verification_email_sent))
                            sendEvent(AuthNavigate(NavRoutes.VerifyEmail))
                        }
                        .onError {
                            _signupState.update {
                                it.copy(loading = false)
                            }
                            sendEvent(AuthShowError(it))
                        }
                }
            }
            is SendCodeToEmail -> onAction(SendCode(_signupState.value.email))
        }
    }
}