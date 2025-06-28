package com.csi.irite.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "District")
data class District(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val name: String,
    val province: String,
    val provinceId: Int
) : BaseEntity()