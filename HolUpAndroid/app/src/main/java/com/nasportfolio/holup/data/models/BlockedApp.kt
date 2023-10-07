package com.nasportfolio.holup.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class BlockedApp(
    @PrimaryKey
    val packageName: String,
    val name: String,
    val coolDown: Int = 10,
    val timeAllowed: Double = 1.0,
    val timeUsed: Double = 0.0,
    val startAccumulatingTime: Boolean = false,
    val lastUpdated: Long = Date().time
)
