package com.csi.irite.room.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "EvidentBag")
data class EvidentBag @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "evident_id") val evident_id: Long?,
    @ColumnInfo(name = "event_report_id") val event_report_id: Long?,
    @ColumnInfo(name = "seq_no") val seq_no: Int?,
    @ColumnInfo(name = "evident") val evident: String = "",
    @ColumnInfo(name = "number") var number: Int = 0,
    @ColumnInfo(name = "place") var place: String="",
    @ColumnInfo(name = "tag_no") var tag_no: String ="",
    @ColumnInfo(name = "plastic") var plastic: Boolean = false,
    @ColumnInfo(name = "paper") var paper: Boolean = false,
    @ColumnInfo(name = "return_pks") var return_pks: String ="",
    @ColumnInfo(name = "remark") var remark: String = ""
) : BaseEntity()