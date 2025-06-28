package com.csi.irite.room.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.time.LocalDateTime
import java.time.ZoneId


@Entity(tableName = "EventReport")
data class EventReport(
    @PrimaryKey(autoGenerate = true)
    @Expose var uid: Long = 0
) :BaseEntity(){

    @ColumnInfo(name = "head")
    @Expose var head: String? = "" //ปจว.ข้อ
    @ColumnInfo(name = "report_number")
    @Expose var report_number: String? ="" //เลขรับ/เลขรายงาน

    @ColumnInfo(name = "report_write")
    @Expose var report_write: String? ="" //1. เขียนที่
    @ColumnInfo(name = "create_datetime")
    @Expose var create_datetime: Long? = 0

    @Expose var create_date: String?= "" //replace create_datetime
    @Expose var create_time: String?= "" //replace create_datetime

    @ColumnInfo(name = "report_from_where")
    @Expose var report_from_where: String?= ""//2. รับแจ้งจาก สน./สภ.

    @ColumnInfo(name = "report_channel")
    @Expose var report_channel: String? = "" //"ทางหนังสือ", "ทางโทรศัพท์","วิทยุสื่อสาร","อื่นๆ",
    @ColumnInfo(name = "report_channel_other")
    @Expose var report_channel_other: String? = ""

    @ColumnInfo(name = "report_type")
    @Expose var report_type: String? = ""
    @ColumnInfo(name = "report_type_other")
    @Expose var report_type_other: String? = ""

    @ColumnInfo(name = "report_place")
    @Expose var report_place: String? = "" //4. สถานที่เกิดเหตุ
    @ColumnInfo(name = "report_distinct")
    @Expose var report_distinct: String? = "" //4. สถานที่เกิดเหตุ
    @ColumnInfo(name = "report_province")
    @Expose var report_province: String? = "" //4. สถานที่เกิดเหตุ

    //@ColumnInfo(name = "report_place_other") val report_place_other: String?, //4. สถานที่เกิดเหตุ
    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnInfo(name = "report_known_datetime")
    @Expose var report_known_datetime: Long? = 0//5. วัน เวลา ผู้เสียหายทราบเหตุ

    @Expose var report_known_date: String? = ""//5. วัน เวลา ผู้เสียหายทราบเหตุ

    @Expose var report_known_time: String? = ""//5. วัน เวลา ผู้เสียหายทราบเหตุ


    @ColumnInfo(name = "report_preinfo")
    @Expose var report_preinfo: String? = "" //6. ข้อมูลเบื้องต้น
    @ColumnInfo(name = "report_officer")
    @Expose var report_officer: String? = ""
    @ColumnInfo(name = "report_officer_contact_no")
    @Expose var report_officer_contact_no: String? = "" //เบอร์โทรศัพท์ติดต่อ
    @ColumnInfo(name = "report_sufferer")
    @Expose var report_sufferer: String? = "" //8. ผู้เสียหาย
    @ColumnInfo(name = "report_sufferer_contact_no")
    @Expose var report_sufferer_contact_no: String? = "" //8. เบอร์โทรศัพท์ติดต่อผู้เสียหาย
    @ColumnInfo(name = "status")
    @Expose var status: Boolean = true //active/inactive
    @Expose var draw_map: ByteArray?= null // store image as byte array
    @Expose var draw_evidence: ByteArray?= null
    @Expose var lat: Double? = 0.0
    @Expose var lng: Double? = 0.0

}