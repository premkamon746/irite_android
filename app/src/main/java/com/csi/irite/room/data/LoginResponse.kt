package com.csi.irite.room.data

data class LoginResponse(
    val success: Boolean,
    val accessToken: String?, // optional if login fails
    val message: String,
    val refreshToken: String
)
