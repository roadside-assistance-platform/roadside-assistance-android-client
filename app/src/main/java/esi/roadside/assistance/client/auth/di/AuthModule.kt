package esi.roadside.assistance.client.auth.di

import org.koin.dsl.module

val authModule = module {
    includes(RepoModule, useCaseModule, viewModelModule)
}