package com.csi.irite.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "History")
data class History(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    val user: String?,
    @ColumnInfo(name = "user_id")
    val userId: String?,
    val action: String?
):BaseEntity()
