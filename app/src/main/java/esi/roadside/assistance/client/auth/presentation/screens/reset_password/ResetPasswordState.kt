package esi.roadside.assistance.client.auth.presentation.screens.reset_password

import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.InputError

data class ResetPasswordState(
    val email: String = "",
    val emailError: InputError? = null,
    val password: String = "",
    val passwordError: InputError? = null,
    val passwordHidden: Boolean = true,
    val confirmPassword: String = "",
    val confirmPasswordError: InputError? = null,
    val confirmPasswordHidden: Boolean = true,
    val userState: ResetPasswordUserState = ResetPasswordUserState.EnterEmail,
    val loading: Boolean = false,
)

enum class ResetPasswordUserState(val title: Int, val text: Int, val buttonText: Int) {
    EnterEmail(R.string.enter_reset_code, R.string.receive_code, R.string.send_reset_code),
    EnterCode(R.string.enter_the_code_text, R.string.send_code_text, R.string.next),
    ResetPassword(R.string.enter_reset_code, R.string.receive_code, R.string.reset),
}