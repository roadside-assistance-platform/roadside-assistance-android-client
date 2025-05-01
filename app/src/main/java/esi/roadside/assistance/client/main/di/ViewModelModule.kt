package esi.roadside.assistance.client.main.di

import esi.roadside.assistance.client.main.presentation.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(androidContext(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}