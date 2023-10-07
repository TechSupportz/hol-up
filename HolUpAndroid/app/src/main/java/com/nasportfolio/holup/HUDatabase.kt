package com.nasportfolio.holup

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nasportfolio.holup.dao.BlockedAppDao
import com.nasportfolio.holup.models.BlockedApp

@Database(
    entities = [BlockedApp::class],
    version = 1
)
abstract class HUDatabase : RoomDatabase() {
    abstract val blockedAppDao: BlockedAppDao
}