package esi.roadside.assistance.client.auth.di

import esi.roadside.assistance.client.auth.domain.use_case.GoogleLogin
import esi.roadside.assistance.client.auth.domain.use_case.Login
import esi.roadside.assistance.client.auth.domain.use_case.ResetPassword
import esi.roadside.assistance.client.auth.domain.use_case.SignUp
import org.koin.dsl.module

val useCaseModule = module {
     factory { ResetPassword(get()) }
     factory { Login(get()) }
     factory { SignUp(get()) }
     factory { GoogleLogin(get()) }
}