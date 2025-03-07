package esi.roadside.assistance.client.auth.di

import com.cloudinary.android.MediaManager
import esi.roadside.assistance.client.auth.data.AuthRepoImpl
import esi.roadside.assistance.client.auth.data.CloudinaryRepoImpl
import esi.roadside.assistance.client.auth.domain.repository.AuthRepo
import esi.roadside.assistance.client.auth.domain.repository.CloudinaryRepo
import esi.roadside.assistance.client.core.data.networking.HttpClientFactory
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val RepoModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single<MediaManager> {
        var config: HashMap<String, String> = HashMap()
        config.put("cloud_name", "dsvlwomdq")
        config.put("api_key", "996654622953693")
        config.put("api_secret", "d8gJrtFaBOSAkSrpaPNU4V4M2-Y")
        MediaManager.init(androidContext(), config)
        MediaManager.get()
    }
    single<AuthRepo> { AuthRepoImpl(get()) }
    single<CloudinaryRepo> { CloudinaryRepoImpl(get()) }
}