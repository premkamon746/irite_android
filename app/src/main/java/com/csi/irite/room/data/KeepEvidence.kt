package com.csi.irite.room.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.csi.irite.room.data.Evidence


@Entity(tableName = "KeepEvidence")
data class KeepEvidence(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
)  : BaseEntity() {
    @ColumnInfo(name = "event_report_id")
    var event_report_id: Long? = 0

    @ColumnInfo(name = "no")
    var no: String? = ""
    @ColumnInfo(name = "evidence")
    var evidence: String? = ""
    @ColumnInfo(name = "distance_1")
    var distance_1: String? = ""
    @ColumnInfo(name = "distance_2")
    var distance_2: String? = ""
    @ColumnInfo(name = "distance_3")
    var distance_3: String? = ""
    @ColumnInfo(name = "distance_4")
    var distance_4: String? = ""
    @ColumnInfo(name = "angle")
    var angle: String? = ""
    @ColumnInfo(name = "remark")
    var remark: String? = ""
    @ColumnInfo(name = "status")
    var status: Boolean? = true
}