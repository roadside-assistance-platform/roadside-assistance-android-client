package esi.roadside.assistance.client.auth.presentation

import esi.roadside.assistance.client.R
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import esi.roadside.assistance.client.auth.domain.models.LoginRequestModel
import esi.roadside.assistance.client.auth.domain.models.SendEmailModel
import esi.roadside.assistance.client.auth.domain.models.SignupModel
import esi.roadside.assistance.client.auth.domain.models.VerifyEmailModel
import esi.roadside.assistance.client.auth.domain.use_case.AuthHome
import esi.roadside.assistance.client.auth.domain.use_case.Cloudinary
import esi.roadside.assistance.client.auth.domain.use_case.Login
import esi.roadside.assistance.client.auth.domain.use_case.ResetPassword
import esi.roadside.assistance.client.auth.domain.use_case.SendEmail
import esi.roadside.assistance.client.auth.domain.use_case.SignUp
import esi.roadside.assistance.client.auth.domain.use_case.Update
import esi.roadside.assistance.client.auth.domain.use_case.VerifyEmail
import esi.roadside.assistance.client.auth.presentation.Action.GoToForgotPassword
import esi.roadside.assistance.client.auth.presentation.Action.GoToLogin
import esi.roadside.assistance.client.auth.presentation.Action.GoToSignup
import esi.roadside.assistance.client.auth.presentation.Action.HideAuthError
import esi.roadside.assistance.client.auth.presentation.Action.Initiate
import esi.roadside.assistance.client.auth.presentation.Action.NextStep
import esi.roadside.assistance.client.auth.presentation.Action.PreviousStep
import esi.roadside.assistance.client.auth.presentation.Action.Send
import esi.roadside.assistance.client.auth.presentation.Action.SendCode
import esi.roadside.assistance.client.auth.presentation.Action.SetCode
import esi.roadside.assistance.client.auth.presentation.Action.SetLoginEmail
import esi.roadside.assistance.client.auth.presentation.Action.SetLoginPassword
import esi.roadside.assistance.client.auth.presentation.Action.SetResetPasswordEmail
import esi.roadside.assistance.client.auth.presentation.Action.SetSignupConfirmPassword
import esi.roadside.assistance.client.auth.presentation.Action.SetSignupEmail
import esi.roadside.assistance.client.auth.presentation.Action.SetSignupFullName
import esi.roadside.assistance.client.auth.presentation.Action.SetSignupImage
import esi.roadside.assistance.client.auth.presentation.Action.SetSignupPassword
import esi.roadside.assistance.client.auth.presentation.Action.SetSignupPhoneNumber
import esi.roadside.assistance.client.auth.presentation.Action.SetVerifyEmailCode
import esi.roadside.assistance.client.auth.presentation.Action.ShowAuthError
import esi.roadside.assistance.client.auth.presentation.Action.Signup
import esi.roadside.assistance.client.auth.presentation.Action.Skip
import esi.roadside.assistance.client.auth.presentation.Action.SkipVerification
import esi.roadside.assistance.client.auth.presentation.Action.ToggleLoginPasswordHidden
import esi.roadside.assistance.client.auth.presentation.Action.ToggleSignupConfirmPasswordHidden
import esi.roadside.assistance.client.auth.presentation.Action.ToggleSignupPasswordHidden
import esi.roadside.assistance.client.auth.presentation.Action.Verify
import esi.roadside.assistance.client.auth.presentation.screens.AuthUiState
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginUiState
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordUiState
import esi.roadside.assistance.client.auth.presentation.screens.signup.OtpState
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupUiState
import esi.roadside.assistance.client.core.data.networking.DomainError
import esi.roadside.assistance.client.auth.util.account.AccountManager
import esi.roadside.assistance.client.auth.util.account.SignInResult
import esi.roadside.assistance.client.auth.util.dataStore
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.core.domain.util.onError
import esi.roadside.assistance.client.core.domain.util.onSuccess
import esi.roadside.assistance.client.core.presentation.util.Event
import esi.roadside.assistance.client.core.presentation.util.Event.AuthNavigate
import esi.roadside.assistance.client.core.presentation.util.Event.AuthShowError
import esi.roadside.assistance.client.core.presentation.util.Event.ImageUploadError
import esi.roadside.assistance.client.core.presentation.util.Event.LaunchMainActivity
import esi.roadside.assistance.client.core.presentation.util.Field
import esi.roadside.assistance.client.core.presentation.util.ValidateInput
import esi.roadside.assistance.client.core.presentation.util.sendEvent
import esi.roadside.assistance.client.main.util.saveClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val context: Context,
    private val cloudinaryUseCase: Cloudinary,
    private val loginUseCase: Login,
    private val signUpUseCase: SignUp,
    private val updateUseCase: Update,
    private val resetPasswordUseCase: ResetPassword,
    private val authHomeUseCase: AuthHome,
    private val sendEmailUseCase: SendEmail,
    private val verifyEmailUseCase: VerifyEmail,
    private val googleIdOption: GetGoogleIdOption,
): ViewModel() {
    private val _step = MutableStateFlow(0)
    val step = _step.asStateFlow()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    private val _signupUiState = MutableStateFlow(SignupUiState())
    val signupUiState = _signupUiState.asStateFlow()

    private val _otpUiState = MutableStateFlow(OtpState())
    val otpUiState = _otpUiState.asStateFlow()

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    private val _resetPasswordUiState = MutableStateFlow(ResetPasswordUiState())
    val resetPasswordUiState = _resetPasswordUiState.asStateFlow()

    private lateinit var accountManager: AccountManager

    fun createAccountManager(activity: Activity) {
        accountManager = AccountManager(activity, googleIdOption)
    }

    fun onOtpAction(action: OtpAction) {
        when(action) {
            is OtpAction.OnChangeFieldFocused -> {
                _otpUiState.update { it.copy(
                    focusedIndex = action.index
                ) }
            }
            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }
            OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(_otpUiState.value.focusedIndex)
                _otpUiState.update { it.copy(
                    code = it.code.mapIndexed { index, number ->
                        if(index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    },
                    focusedIndex = previousIndex
                ) }
            }
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = _otpUiState.value.code.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _otpUiState.update { it.copy(
            code = newCode,
            focusedIndex = if(wasNumberRemoved || it.code.getOrNull(index) != null) {
                it.focusedIndex
            } else {
                getNextFocusedTextFieldIndex(
                    currentCode = it.code,
                    currentFocusedIndex = it.focusedIndex
                )
            }
        ) }
        if (_otpUiState.value.code.none { it == null }) {
            onAction(Verify)
        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if(currentFocusedIndex == null) {
            return null
        }

        if(currentFocusedIndex == _otpUiState.value.code.size - 1) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if(number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }

    fun onAction(action: Action) {
        when(action) {
            Initiate -> {
                _authUiState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    context.dataStore.data.firstOrNull()?.let { userPreferences ->
                        authHomeUseCase()
                            .onSuccess {
                                _authUiState.update {
                                    it.copy(
                                        error = null,
                                        errorDialogVisible = false,
                                        loading = false
                                    )
                                }
                                if (it) loggedIn(userPreferences.client.toClientModel())
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
                viewModelScope.launch {
                    val result = accountManager.signIn()
                    if (result is SignInResult.Success) {
                        _loginUiState.update {
                            it.copy(email = result.username, password = result.password)
                        }
                    }
                }
            }
            is GoToSignup -> {
                sendEvent(AuthNavigate(NavRoutes.Signup))
                viewModelScope.launch {
                    val result = accountManager.signIn()
                    if (result is SignInResult.Success) {
                        _signupUiState.update {
                            it.copy(
                                email = result.username,
                                password = result.password,
                                confirmPassword = result.password
                            )
                        }
                    }
                }
            }
            is GoToForgotPassword -> {
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
                        loggedIn(it.user)
                    }.onError {
                        println(it)
                        onAction(ShowAuthError(it))
                        _loginUiState.update {
                            it.copy(loading = false)
                        }
                    }
                }
            }
            is Signup -> {
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
                } else {
                    _signupUiState.update {
                        it.copy(loading = true)
                    }
                    viewModelScope.launch {
                        var url: String? = null
                        cloudinaryUseCase(
                            image = _signupUiState.value.image ?: "".toUri(),
                            onSuccess = {
                                Log.i("Welcome", "Image uploaded successfully: $it")
                                url = it
                            },
                            onProgress = { progress ->
                                _signupUiState.update {
                                    it.copy(uploadProgress = progress)
                                }
                            },
                            onFailure = {
                                sendEvent(ImageUploadError)
                            },
                            onFinished = {
                                _signupUiState.update {
                                    it.copy(photo = url ?: "_")
                                }
                                sendEvent(AuthNavigate(NavRoutes.VerifyEmail))
                                onAction(SendCode(_signupUiState.value.email))
                            }
                        )
                    }
                }
            }
            is Action.SendCodeToEmail -> onAction(SendCode(_signupUiState.value.email))
            is SendCode -> {
                viewModelScope.launch {
                    sendEmailUseCase(SendEmailModel(action.email))
                        .onSuccess {
                            sendEvent(Event.ShowAuthActivityMessage(R.string.verification_email_sent))
                            _signupUiState.update {
                                it.copy(loading = false)
                            }
                        }
                        .onError {
                            _signupUiState.update {
                                it.copy(loading = false)
                            }
                            onAction(ShowAuthError(it))
                        }
                }
            }
            is Verify -> {
                _signupUiState.update {
                    it.copy(loading = true)
                }
                viewModelScope.launch {
                    verifyEmailUseCase(
                        VerifyEmailModel(
                            _signupUiState.value.email,
                            _otpUiState.value.code.filterNotNull().joinToString(""),
                        )
                    ).onSuccess {
                        signUpUseCase(
                            SignupModel(
                                email = _signupUiState.value.email,
                                password = _signupUiState.value.password,
                                fullName = _signupUiState.value.fullName,
                                phone = _signupUiState.value.phoneNumber,
                                photo = _signupUiState.value.photo,
                            )
                        ).onSuccess { response ->
                            accountManager.signUp(
                                _signupUiState.value.email,
                                _signupUiState.value.password
                            )
                            _signupUiState.update { it.copy(loading = false) }
                            loggedIn(response.user)
                        }.onError {
                            _signupUiState.update {
                                it.copy(loading = false)
                            }
                            onAction(ShowAuthError(it))
                        }
                    }.onError {
                        _signupUiState.update {
                            it.copy(loading = false)
                        }
                        onAction(ShowAuthError(it))
                    }
                }
            }
            is Send -> {
                viewModelScope.launch {
                    resetPasswordUseCase(_resetPasswordUiState.value.email)
                }
            }
            is SetResetPasswordEmail -> {
                _resetPasswordUiState.update {
                    it.copy(email = action.email)
                }
            }
            is SetCode -> {
                _resetPasswordUiState.update {
                    it.copy(code = action.code)
                }
            }
            is SetLoginEmail -> {
                _loginUiState.update {
                    it.copy(email = action.email)
                }
            }
            is SetLoginPassword -> {
                _loginUiState.update {
                    it.copy(password = action.password)
                }
            }

            is SetSignupConfirmPassword -> {
                _signupUiState.update {
                    it.copy(confirmPassword = action.confirmPassword)
                }
            }
            is SetSignupEmail -> {
                _signupUiState.update {
                    it.copy(email = action.email)
                }
            }
            is SetSignupFullName -> {
                _signupUiState.update {
                    it.copy(fullName = action.fullName)
                }
            }
            is SetSignupImage -> {
                _signupUiState.update {
                    it.copy(image = action.image)
                }
            }
            is SetSignupPassword -> {
                _signupUiState.update {
                    it.copy(password = action.password)
                }
            }

            is SetSignupPhoneNumber -> {
                _signupUiState.update {
                    it.copy(phoneNumber = action.phoneNumber)
                }
            }
            is SetVerifyEmailCode -> {
                _signupUiState.update {
                    it.copy(verifyEmailCode = action.code)
                }
            }
            ToggleLoginPasswordHidden -> {
                _loginUiState.update {
                    it.copy(passwordHidden = !it.passwordHidden)
                }
            }
            ToggleSignupConfirmPasswordHidden -> {
                _signupUiState.update {
                    it.copy(confirmPasswordHidden = !it.confirmPasswordHidden)
                }
            }
            ToggleSignupPasswordHidden -> {
                _signupUiState.update {
                    it.copy(passwordHidden = !it.passwordHidden)
                }
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

    private fun loggedIn(client: ClientModel, launchMainActivity: Boolean = true) {
        Log.i("Welcome", "Logged in successfully: $client")
        saveClient(context, client)
        if (launchMainActivity) sendEvent(LaunchMainActivity)
    }
}