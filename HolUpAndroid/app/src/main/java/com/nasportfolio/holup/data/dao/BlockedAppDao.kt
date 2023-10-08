package com.nasportfolio.holup.data.dao

import com.nasportfolio.holup.data.models.BlockedApp
import kotlinx.coroutines.flow.Flow

interface BlockedAppDao {
    fun getAllBlockedApps(): Flow<List<BlockedApp>>
    suspend fun upsertBlockedApp(blockedApp: BlockedApp)
    suspend fun deleteBlockedApp(blockedApp: BlockedApp)
    suspend fun deleteAppByPackageName(packageName: String)
}