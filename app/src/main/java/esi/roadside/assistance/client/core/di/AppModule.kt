package esi.roadside.assistance.client.core.di

import esi.roadside.assistance.client.core.data.SettingsDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        SettingsDataStore(androidContext())
    }
}