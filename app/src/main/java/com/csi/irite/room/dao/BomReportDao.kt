package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.data.BomReport
import com.csi.irite.room.data.EventReport

@Dao
interface BomReportDao {
    @Insert
    fun insertBomReport(bomReport: BomReport)

    @Update
    fun updateIncidentReport(bomReport: BomReport)

    @Query("SELECT * FROM BomReport")
    fun getAllAssetReports(): List<BomReport>

    @Query("SELECT * FROM BomReport WHERE uid = :id")
    fun getBomReportById(id: Long): BomReport?

    @Query("SELECT * FROM BomReport WHERE event_report_id = :event_report_id")
    fun getBomReportByEventId(event_report_id: Long): BomReport?

    @RawQuery
    fun updateField(query: SupportSQLiteQuery): Int

    @Insert
    fun insertAll(events: BomReport): Long

    @Update
    fun updateAll(events: BomReport)

    @Query("UPDATE BomReport SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""
    SELECT BomReport.* FROM BomReport
    INNER JOIN EventReport ON BomReport.event_report_id = EventReport.uid
    WHERE status = 1
    """)
    suspend fun getUnsyncedData(): List<BomReport>

    @Query("SELECT * FROM BomReport WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): BomReport?


}