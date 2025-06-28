package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.csi.irite.room.data.AssetForm
import com.csi.irite.room.data.EventReport

@Dao
interface AssetFormDao {
    @Query("SELECT * FROM AssetForm")
    fun getAll(): List<AssetForm>

    @Query("SELECT * FROM AssetForm WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<AssetForm>

    @Query("SELECT * FROM AssetForm WHERE event_report_id  = :event_report_id")
    fun getByIds(event_report_id: Long): List<AssetForm>

    @Query("SELECT * FROM AssetForm WHERE event_report_id = :event_report_id")
    fun findEventId(event_report_id: String):  List<AssetForm>

    @Insert
    fun insertAll(vararg users: AssetForm)

    @Delete
    fun delete(checklist: AssetForm)

    @Update
    fun updateAll(assetForm: AssetForm)

    @Query("UPDATE AssetForm SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""
    SELECT AssetForm.* FROM AssetForm
    INNER JOIN EventReport ON AssetForm.event_report_id = EventReport.uid
    WHERE status = 1
    """)
    suspend fun getUnsyncedData(): List<EventReport>

    @Query("SELECT * FROM AssetForm WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): EventReport?
}