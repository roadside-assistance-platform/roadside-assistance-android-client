package esi.roadside.assistance.client.main.di

import org.koin.dsl.module

val mainModule = module {
    includes(viewModelModule, useCaseModule, repoModule)
}