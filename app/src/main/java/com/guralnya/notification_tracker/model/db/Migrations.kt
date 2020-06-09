package com.guralnya.notification_tracker.model.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `ignorePackages` (`packageName` TEXT PRIMARY KEY NOT NULL)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE VIEW `IgnoreAppVo` AS SELECT DISTINCT appPackageName FROM notifyInfo INNER JOIN ignorePackages ON packageName == appPackageName")
    }
}