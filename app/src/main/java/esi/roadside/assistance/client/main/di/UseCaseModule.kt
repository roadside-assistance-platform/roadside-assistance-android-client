package esi.roadside.assistance.client.main.di

import esi.roadside.assistance.client.NotificationService
import esi.roadside.assistance.client.main.domain.use_cases.Completion
import esi.roadside.assistance.client.main.domain.use_cases.DirectionsUseCase
import esi.roadside.assistance.client.main.domain.use_cases.FetchServices
import esi.roadside.assistance.client.main.domain.use_cases.Geocoding
import esi.roadside.assistance.client.main.domain.use_cases.Logout
import esi.roadside.assistance.client.main.domain.use_cases.Rating
import esi.roadside.assistance.client.main.domain.use_cases.SubmitRequest
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val useCaseModule = module {
    factory { SubmitRequest(get()) }
    factory { Logout(get()) }
    factory { Geocoding(get()) }
    factory { Completion(get()) }
    factory { DirectionsUseCase(get()) }
    factory { Rating(get()) }
    factory { NotificationService(androidContext()) }
    factory { FetchServices(get(), get()) }
}