package com.csi.irite.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey val uid: Long,
    val user: String,
    val name: String,
    val createdat: Long,
    val role: String
)