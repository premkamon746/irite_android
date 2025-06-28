package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.FireReport

@Dao
interface FireReportDao {
    @Insert
    fun insertFireReport(fireReport: FireReport)

    @Update
    fun updateFireReport(fireReport: FireReport)

    @Update
    suspend fun updateIncidentReport(fireReport: FireReport)

    @Query("SELECT * FROM FireReport")
    fun getAllFireReports(): List<FireReport>

    @Query("SELECT * FROM FireReport WHERE uid = :id")
    fun getFireReportById(id: Long): FireReport?

    @Query("SELECT * FROM FireReport WHERE event_report_id = :event_report_id")
    fun getFireReportByEventId(event_report_id: Long): FireReport?

    @RawQuery
    fun updateField(query: SupportSQLiteQuery): Int

    @Insert
    fun insertAll(events: FireReport): Long

    @Update
    fun updateAll(events: FireReport)

    @Query("UPDATE FireReport SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""
    SELECT FireReport.* FROM FireReport
    INNER JOIN EventReport ON FireReport.event_report_id = EventReport.uid
    WHERE status = 1
    """)
    suspend fun getUnsyncedData(): List<FireReport>

    @Query("SELECT * FROM FireReport WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): FireReport?

}