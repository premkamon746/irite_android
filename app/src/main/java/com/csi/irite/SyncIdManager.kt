package com.csi.irite

import android.content.Context
import java.util.UUID

object SyncIdManager {

    private const val PREFS_NAME = "device_prefs"
    private const val DEVICE_ID_KEY = "device_uuid"

    // 1. Get or create persistent device ID
    fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var deviceId = prefs.getString(DEVICE_ID_KEY, null)

        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString(DEVICE_ID_KEY, deviceId).apply()
        }

        return deviceId
    }

    // 2. Generate sync ID using device ID + UUID (or timestamp)
    fun generateSyncId(context: Context): String {
        val deviceId = getDeviceId(context)
        val randomUuid = UUID.randomUUID().toString()
        return "$deviceId-$randomUuid"
    }

    // Optional: timestamp-based sync ID for ordered events
    fun generateSyncIdWithTimestamp(context: Context): String {
        val deviceId = getDeviceId(context)
        val timestamp = System.currentTimeMillis()
        return "$deviceId-$timestamp"
    }
}
