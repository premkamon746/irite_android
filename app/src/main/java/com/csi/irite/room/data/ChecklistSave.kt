package com.csi.irite.room.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ChecklistSave")
data class ChecklistSave @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "checklist_head_id")  var checklist_head_id: Long?,
    @ColumnInfo(name = "head") val head: String?,
    @ColumnInfo(name = "listname") val listname: String?,
    @ColumnInfo(name = "sorting") val sorting: Int?,
    @ColumnInfo(name = "gropping") val gropping: String?,
    @ColumnInfo(name = "editing") val editing: String?,
    @ColumnInfo(name = "remark") val remark: String?,
    @ColumnInfo(name = "status") val status: Boolean?,
    @ColumnInfo(name = "create_datetime") var create_datetime: Long?
) : BaseEntity()

