package com.guralnya.notification_tracker.di

import android.content.Context
import androidx.room.Room
import com.guralnya.notification_tracker.model.db.NotifyTrackerDb
import com.guralnya.notification_tracker.model.settings.KeystoreManager
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

object Modules {

    fun getDb(context: Context, keystoreManager: KeystoreManager): NotifyTrackerDb {
        val passphrase = SQLiteDatabase.getBytes(keystoreManager.getDbPass()?.toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(context, NotifyTrackerDb::class.java, "notifyTracker.db")
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }
}