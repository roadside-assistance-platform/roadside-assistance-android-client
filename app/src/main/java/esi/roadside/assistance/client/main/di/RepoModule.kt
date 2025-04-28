package esi.roadside.assistance.client.main.di

import esi.roadside.assistance.client.main.data.networking.MainRepoImpl
import esi.roadside.assistance.client.main.domain.repository.MainRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single<MainRepo> { MainRepoImpl(androidContext(), get(), get()) }
}