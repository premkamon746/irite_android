package com.csi.irite.room.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.csi.irite.room.data.EventReport
import java.time.LocalDateTime
import java.time.ZoneId


@Entity(tableName = "TranferEvidence")
data class TranferEvidence(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
)  : BaseEntity() {
    @ColumnInfo(name = "event_report_id")
    var event_report_id: Long? = 0

    @ColumnInfo(name = "head")
    var head: String? = "" //ปจว.ข้อ
    @ColumnInfo(name = "report_number")
    var report_number: String? ="" //เลขรับ/เลขรายงาน

    @ColumnInfo(name = "report_write")
    var report_write: String? ="" //1. เขียนที่
    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnInfo(name = "create_datetime")
    var create_datetime: Long? = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()

    @ColumnInfo(name = "report_from_where")
    var report_from_where: String?= ""//2. รับแจ้งจาก สน./สภ.

    @ColumnInfo(name = "report_channel")
    var report_channel: String? = "" //"ทางหนังสือ", "ทางโทรศัพท์","วิทยุสื่อสาร","อื่นๆ",
    @ColumnInfo(name = "report_channel_other")
    var report_channel_other: String? = ""

    @ColumnInfo(name = "report_type")
    var report_type: String? = ""
    @ColumnInfo(name = "report_type_other")
    var report_type_other: String? = ""

    @ColumnInfo(name = "report_place")
    var report_place: String? = "" //4. สถานที่เกิดเหตุ

    //@ColumnInfo(name = "report_place_other") val report_place_other: String?, //4. สถานที่เกิดเหตุ
    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnInfo(name = "report_known_datetime")
    var report_known_datetime: Long? = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli() //5. วัน เวลา ผู้เสียหายทราบเหตุ
    @ColumnInfo(name = "report_preinfo")
    var report_preinfo: String? = "" //6. ข้อมูลเบื้องต้น
    @ColumnInfo(name = "report_officer")
    var report_officer: String? = ""
    @ColumnInfo(name = "report_officer_contact_no")
    var report_officer_contact_no: String? = "" //เบอร์โทรศัพท์ติดต่อ
    @ColumnInfo(name = "report_sufferer")
    var report_sufferer: String? = "" //8. ผู้เสียหาย
    @ColumnInfo(name = "report_sufferer_contact_no")
    var report_sufferer_contact_no: String? = "" //8. เบอร์โทรศัพท์ติดต่อผู้เสียหาย
    @ColumnInfo(name = "status")
    var status: String = "active" //active/inactive
}