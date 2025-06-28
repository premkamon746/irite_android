package com.csi.irite.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// LoginRequest.kt
@Entity(tableName = "LoginRequest")
data class LoginRequest(
    val username: String,
    val password: String
)
