package com.nasportfolio.holup.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlockedApp(
    @PrimaryKey
    val packageName: String,
    val name: String,
)
