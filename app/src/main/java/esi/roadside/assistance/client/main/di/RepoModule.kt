package esi.roadside.assistance.client.main.di

import esi.roadside.assistance.client.main.data.networking.GeocodingRepoImpl
import esi.roadside.assistance.client.main.data.networking.MainRepoImpl
import esi.roadside.assistance.client.main.domain.repository.GeocodingRepo
import esi.roadside.assistance.client.main.domain.repository.MainRepo
import esi.roadside.assistance.client.main.domain.repository.ServiceManager
import esi.roadside.assistance.client.main.domain.services.VehicleIssueAIService
import esi.roadside.assistance.client.main.util.QueuesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single<MainRepo> { MainRepoImpl(get(), get(), get()) }
    single<GeocodingRepo> { GeocodingRepoImpl(androidContext(), get()) }
    single<QueuesManager> { QueuesManager() }
    single<ServiceManager> {
        ServiceManager(androidContext(), get(), get(), get(), get(), get(), get())
    }
    single { VehicleIssueAIService(androidContext()) }
}