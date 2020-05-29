package com.guralnya.notification_tracker

import android.app.Application
import com.guralnya.notification_tracker.di.DependencyInjector
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this);
        startKoin(this, listOf(DependencyInjector.appModule))
    }
}