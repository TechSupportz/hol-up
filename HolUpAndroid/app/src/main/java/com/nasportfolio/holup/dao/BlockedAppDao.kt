package com.nasportfolio.holup.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nasportfolio.holup.models.BlockedApp
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedAppDao {

    @Query("SELECT * FROM BlockedApp")
    fun getAllBlockedApps(): Flow<List<BlockedApp>>

    @Upsert
    suspend fun upsertBlockedApp(blockedApp: BlockedApp)

    @Delete
    suspend fun deleteBlockedApp(blockedApp: BlockedApp)

    @Query("DELETE FROM BlockedApp WHERE packageName = :packageName")
    suspend fun deleteAppByPackageName(packageName: String)
}