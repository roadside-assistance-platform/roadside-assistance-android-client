package esi.roadside.assistance.client.core.di

import esi.roadside.assistance.client.auth.presentation.AuthViewModel
import esi.roadside.assistance.client.core.data.SettingsDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val coreModule = module {
    single { SettingsDataStore(androidContext()) }
    // not here ,impl in authModule
    //viewModelOf(::AuthViewModel)
}