package com.guralnya.notification_tracker.model.db.dao

import androidx.room.*
import com.guralnya.notification_tracker.model.models.IgnorePackage
import com.guralnya.notification_tracker.model.models.vo.IgnoreAppVo

@Dao
interface IgnorePackagesDao {

    @Query("SELECT * FROM IgnoreAppVo")
    fun getListPackagesWithIgnore(): List<IgnoreAppVo>

    @Query("SELECT * FROM ignorePackages")
    fun getIgnorePackages(): List<IgnorePackage>

    @Query("DELETE FROM ignorePackages") //TODO in new version Room use: DROP TABLE ignorePackages
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIgnorePackages(list: List<IgnorePackage>)

    @Delete
    fun delete(list: List<IgnorePackage>)
}