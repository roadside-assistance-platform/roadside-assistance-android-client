package esi.roadside.assistance.client.main.di

import esi.roadside.assistance.client.main.domain.use_cases.Logout
import esi.roadside.assistance.client.main.domain.use_cases.SubmitRequest
import org.koin.dsl.module

val useCaseModule = module {
    factory { SubmitRequest(get()) }
    factory { Logout(get()) }
}