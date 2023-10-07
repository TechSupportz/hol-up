package com.nasportfolio.holup.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nasportfolio.holup.data.dao.BlockedAppDao
import com.nasportfolio.holup.data.models.BlockedApp

@Database(
    entities = [BlockedApp::class],
    version = 1
)
abstract class HUDatabase : RoomDatabase() {
    abstract val blockedAppDao: BlockedAppDao
}