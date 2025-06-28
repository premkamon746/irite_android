package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.data.AssetReport
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.LifeReport

@Dao
interface LifeReportDao {
    @Insert
    fun insertLifeReport(lifeReport: LifeReport)

    @Update
    fun updateLifeReport(lifeReport: LifeReport)

    @Query("SELECT * FROM LifeReport")
    fun getAllAssetReports(): List<LifeReport>

    @Query("SELECT * FROM LifeReport WHERE uid = :id")
    fun getLifeReportById(id: Long): LifeReport?

    @Query("SELECT * FROM LifeReport WHERE event_report_id = :event_report_id")
    fun getLifeReportByEventId(event_report_id: Long): LifeReport?

    @RawQuery
    fun updateField(query: SupportSQLiteQuery): Int

    @Insert
    fun insertAll(events: LifeReport): Long

    @Update
    fun updateAll(events: LifeReport)

    @Query("UPDATE LifeReport SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""
    SELECT LifeReport.* FROM LifeReport
    INNER JOIN EventReport ON LifeReport.event_report_id = EventReport.uid
    WHERE status = 1
    """)
    suspend fun getUnsyncedData(): List<LifeReport>

    @Query("SELECT * FROM LifeReport WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): LifeReport?

}