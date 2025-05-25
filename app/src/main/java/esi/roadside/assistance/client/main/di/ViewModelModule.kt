package esi.roadside.assistance.client.main.di

import esi.roadside.assistance.client.main.presentation.MainViewModel
import esi.roadside.assistance.client.main.presentation.routes.home.SearchViewModel
import esi.roadside.assistance.client.main.presentation.routes.home.request.AssistanceViewModel
import esi.roadside.assistance.client.main.presentation.routes.profile.ProfileViewModel
import esi.roadside.assistance.client.main.presentation.routes.settings.ChangePasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get(), get(), get(), get(), get())
    }
    viewModel {
        ChangePasswordViewModel(get(), get())
    }
    viewModel {
        AssistanceViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        ProfileViewModel(get(), get(), get())
    }
}