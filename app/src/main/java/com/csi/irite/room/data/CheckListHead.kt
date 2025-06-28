package com.csi.irite.room.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CheckListHead")
data class CheckListHead @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "datetime") val datetime: Long?,
    @ColumnInfo(name = "team") val team: String = "",
    @ColumnInfo(name = "complete") var complete: Boolean?,
    @ColumnInfo(name = "remark") var remark: String?,
    @ColumnInfo(name = "car_license") var car_license: String?,
    @ColumnInfo(name = "no_of_kilo") var no_of_kilo: Int?,
    @ColumnInfo(name = "create_datetime") var create_datetime: Long?,
    @ColumnInfo(name = "status") var status: Boolean?,
) : BaseEntity()