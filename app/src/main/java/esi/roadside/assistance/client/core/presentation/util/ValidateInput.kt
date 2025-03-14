package esi.roadside.assistance.client.core.presentation.util

import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.auth.presentation.screens.login.InputError

object ValidateInput {
    fun validateEmail(email: String): InputError? =
        when {
            email.isEmpty() -> {
                InputError.Empty(Field.EMAIL, R.string.error_empty_email)
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                InputError.Invalid(Field.EMAIL, R.string.error_invalid_email)
            }
            else -> null
        }

    fun validatePassword(password: String): InputError? =
        when {
            password.isEmpty() -> {
                InputError.Empty(Field.PASSWORD, R.string.error_empty_password)
            }
            password.length < 6 -> {
                InputError.Short(Field.PASSWORD, R.string.error_short_password)
            }
            else -> null
        }

    fun validateFullName(fullName: String): InputError? =
        when {
            fullName.isEmpty() -> {
                InputError.Empty(Field.FULL_NAME, R.string.error_empty_full_name)
            }
            else -> null
        }

    fun validateConfirmPassword(password: String, confirmPassword: String): InputError? =
        when {
            confirmPassword.isEmpty() -> {
                InputError.Empty(Field.CONFIRM_PASSWORD, R.string.error_empty_confirm_password)
            }
            password != confirmPassword -> {
                InputError.Mismatch(Field.CONFIRM_PASSWORD, R.string.error_password_mismatch)
            }
            else -> null
        }

    fun validatePhoneNumber(phoneNumber: String): InputError? =
        when {
            phoneNumber.isEmpty() -> {
                InputError.Empty(Field.PHONE_NUMBER, R.string.error_empty_phone_number)
            }
            phoneNumber.length < 10 -> {
                InputError.Short(Field.PHONE_NUMBER, R.string.error_short_phone_number)
            }
            !android.util.Patterns.PHONE.matcher(phoneNumber).matches() -> {
                InputError.Invalid(Field.PHONE_NUMBER, R.string.error_invalid_phone_number)
            }
            else -> null
        }

    fun validateLogin(email: String, password: String): Pair<Field, InputError?>? {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)

        return when {
            emailError != null -> {
                Field.EMAIL to emailError
            }
            passwordError != null -> {
                Field.PASSWORD to passwordError
            }
            else -> null
        }
    }

    fun validateSignup(
        email: String,
        password: String,
        confirmPassword: String,
        fullName: String,
        phoneNumber: String
    ): InputError? =
        validateFullName(fullName) ?: validatePhoneNumber(phoneNumber) ?:
        validateEmail(email) ?: validatePassword(password) ?:
        validateConfirmPassword(password, confirmPassword)
}