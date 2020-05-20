package com.guralnya.notification_tracker

import android.app.Application
import com.guralnya.notification_tracker.di.DependencyInjector
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(DependencyInjector.appModule))
    }
}