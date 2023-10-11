package com.guralnya.notification_tracker

import android.app.Application
import com.guralnya.notification_tracker.di.DependencyInjector
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this);
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(DependencyInjector.appModule)}
    }
}