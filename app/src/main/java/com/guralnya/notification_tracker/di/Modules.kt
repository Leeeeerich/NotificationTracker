package com.guralnya.notification_tracker.di

import android.content.Context
import androidx.room.Room
import com.guralnya.notification_tracker.BuildConfig
import com.guralnya.notification_tracker.model.db.MIGRATION_1_2
import com.guralnya.notification_tracker.model.db.MIGRATION_2_3
import com.guralnya.notification_tracker.model.db.NotifyTrackerDb
import com.guralnya.notification_tracker.model.settings.KeystoreManager
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

object Modules {

    fun getDb(context: Context, keystoreManager: KeystoreManager): NotifyTrackerDb {
        val passphrase = SQLiteDatabase.getBytes(
            (
                    if (BuildConfig.DEBUG) BuildConfig.DB_PASS_ALIAS
                    else keystoreManager.getDbPass())?.toCharArray()
        )
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(context, NotifyTrackerDb::class.java, "notifyTracker.db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .openHelperFactory(factory)
            .build()
    }
}