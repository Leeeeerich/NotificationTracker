package com.guralnya.notification_tracker.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ignorePackages")
data class IgnorePackage(
    @PrimaryKey val packageName: String
)