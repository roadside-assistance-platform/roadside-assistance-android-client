package esi.roadside.assistance.client.auth.di

import esi.roadside.assistance.client.auth.presentation.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // impl here in authModule instead of coreModule
    //viewModelOf(::AuthViewModel)
    viewModel { AuthViewModel(get(), get(), get(), get(), get(),get()) }

}