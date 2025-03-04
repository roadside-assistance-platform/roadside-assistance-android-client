package esi.roadside.assistance.client

import android.app.Application
import esi.roadside.assistance.client.auth.di.authModule
import esi.roadside.assistance.client.core.di.coreModule
import esi.roadside.assistance.client.main.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(coreModule, authModule, mainModule)
        }
    }
}