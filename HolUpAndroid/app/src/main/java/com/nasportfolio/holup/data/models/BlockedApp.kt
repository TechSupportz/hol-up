package com.nasportfolio.holup.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity
data class BlockedApp(
    @PrimaryKey
    var packageName: String = System.currentTimeMillis().toString(),
    var name: String,
    var cooldown: Int = 10,
    var timeAllowed: Double = 1.0,
    var timeUsed: Double = 0.0,
    var userId: String? = null,
    @DocumentId
    var id: String? = null,
) {
    constructor(): this(
        packageName = "",
        name = "",
        cooldown = 10,
        timeAllowed = 1.0,
        timeUsed = 0.0,
        userId = null,
        id = null,
    )
}
