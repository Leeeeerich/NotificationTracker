package com.guralnya.notification_tracker.di

import com.guralnya.notification_tracker.model.repository.Repository
import com.guralnya.notification_tracker.model.repository.RepositoryImpl
import com.guralnya.notification_tracker.model.settings.PreferencesManager
import com.guralnya.notification_tracker.model.settings.PreferencesManagerImpl
import com.guralnya.notification_tracker.ui.MainViewModel
import com.guralnya.notification_tracker.ui.home_screen.HomeViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object DependencyInjector {

    val appModule = module {
        single { Modules.getDb(get()) }
        single<PreferencesManager> { PreferencesManagerImpl(get()) }
        single<Repository> { RepositoryImpl(get()) }
        viewModel { MainViewModel() }
        viewModel { HomeViewModel(get(), get()) }
    }
}