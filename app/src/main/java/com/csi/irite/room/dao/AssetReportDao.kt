package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.data.AssetReport
import com.csi.irite.room.data.EventReport

@Dao
interface AssetReportDao {
    @Insert
    fun insertIncidentReport(AssetReport: AssetReport)

    @Update
    fun updateIncidentReport(AssetReport: AssetReport)

    @Query("SELECT * FROM AssetReport")
    fun getAllAssetReports(): List<AssetReport>

    @Query("SELECT * FROM AssetReport WHERE uid = :id")
    fun getAssetReportById(id: Long): AssetReport?

    @Query("SELECT * FROM AssetReport WHERE event_report_id = :event_report_id")
    fun getAssetReportByEventId(event_report_id: Long): AssetReport?

    @RawQuery
    fun updateField(query: SupportSQLiteQuery): Int

    @Insert
    fun insertAll(events: AssetReport): Long

    @Update
    fun updateAll(events: AssetReport)

    @Query("UPDATE AssetReport SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""
    SELECT AssetReport.* FROM AssetReport
    INNER JOIN EventReport ON AssetReport.event_report_id = EventReport.uid
    WHERE status = 1
    """)
    suspend fun getUnsyncedData(): List<AssetReport>

    @Query("SELECT * FROM AssetReport WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): AssetReport?

}