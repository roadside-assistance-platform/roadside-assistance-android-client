package esi.roadside.assistance.client.auth.di

import esi.roadside.assistance.client.auth.presentation.AuthViewModel
import esi.roadside.assistance.client.auth.presentation.screens.login.LoginViewModel
import esi.roadside.assistance.client.auth.presentation.screens.reset_password.ResetPasswordViewModel
import esi.roadside.assistance.client.auth.presentation.screens.signup.SignupViewModel
import esi.roadside.assistance.client.core.util.account.AccountManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        AccountManager(androidContext())
    }
    viewModel {
        AuthViewModel(get(), get())
    }
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        SignupViewModel(get(), get(), get(), get(), get())
    }
    viewModel {
        ResetPasswordViewModel(get(), get(), get())
    }
}