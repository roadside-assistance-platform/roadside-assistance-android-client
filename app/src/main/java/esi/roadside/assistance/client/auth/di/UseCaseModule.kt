package esi.roadside.assistance.client.auth.di

import esi.roadside.assistance.client.auth.domain.use_case.AuthHome
import esi.roadside.assistance.client.auth.domain.use_case.Cloudinary
import esi.roadside.assistance.client.auth.domain.use_case.GoogleLogin
import esi.roadside.assistance.client.auth.domain.use_case.GoogleOldLogin
import esi.roadside.assistance.client.auth.domain.use_case.Home
import esi.roadside.assistance.client.auth.domain.use_case.Login
import esi.roadside.assistance.client.auth.domain.use_case.ResetPassword
import esi.roadside.assistance.client.auth.domain.use_case.SignUp
import esi.roadside.assistance.client.auth.domain.use_case.Update
import org.koin.dsl.module

val useCaseModule = module {
     factory { ResetPassword(get()) }
     factory { Login(get()) }
     factory { SignUp(get()) }
     factory { Update(get()) }
     factory { Home(get()) }
     factory { GoogleLogin(get()) }
     factory { GoogleOldLogin(get()) }
     factory { Cloudinary(get()) }
     factory { AuthHome(get()) }
}