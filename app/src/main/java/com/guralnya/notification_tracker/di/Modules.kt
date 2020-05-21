package com.guralnya.notification_tracker.di

import android.content.Context
import androidx.room.Room
import com.guralnya.notification_tracker.model.db.NotifyTrackerDb

object Modules {

    fun getDb(context: Context): NotifyTrackerDb {
//        val passphrase = SQLiteDatabase.getBytes("VitalAppKeyCipher".toCharArray())
//        val factory = SupportFactory(passphrase) //TODO need implementation cypher DB
        return Room.databaseBuilder(
            context,
            NotifyTrackerDb::class.java,
            "notifyTracker.db"
        )
//            .openHelperFactory(factory)
//            .fallbackToDestructiveMigration()
            .build()
    }
}