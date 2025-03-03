package esi.roadside.assistance.client.auth.di

import esi.roadside.assistance.client.auth.data.AuthRepoImpl
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.core.data.networking.HttpClientFactory
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val RepoModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single<AuthRepo> { AuthRepoImpl(get()) }
}