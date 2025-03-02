package esi.roadside.assistance.client.core.di

import esi.roadside.assistance.client.auth.data.AuthRepoImpl
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.auth.presentation.NavRoutes.Welcome
import esi.roadside.assistance.client.auth.presentation.WelcomeViewModel
import esi.roadside.assistance.client.core.data.SettingsDataStore
import esi.roadside.assistance.client.core.data.networking.HttpClientFactory
import esi.roadside.assistance.client.main.data.networking.ClientRepoImpl
import esi.roadside.assistance.client.main.domain.repository.ClientRepo
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::ClientRepoImpl).bind<ClientRepo>()
    singleOf(::AuthRepoImpl).bind<AuthRepo>()
    single { SettingsDataStore(androidContext()) }
    viewModelOf(::WelcomeViewModel)
}