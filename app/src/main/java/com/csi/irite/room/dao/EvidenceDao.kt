package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.Evidence
import com.csi.irite.room.data.EvidentBag

@Dao
interface EvidenceDao {
    @Query("SELECT * FROM Evidence")
    fun getAll(): MutableList<Evidence>

    @Query("SELECT * FROM Evidence WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): MutableList<Evidence>

    @Query("SELECT * FROM Evidence WHERE uid IN (:evIDs)")
    fun getSomeChecked(evIDs: LongArray): List<Evidence>


    @Query("SELECT * FROM Evidence WHERE event_report_id  = :event_report_id and status = :isActive ")
    fun getByIds(event_report_id: Long, isActive: Boolean): List<Evidence>

    @Query("SELECT * FROM Evidence WHERE event_report_id = :event_report_id")
    fun findEventId(event_report_id: Long):  List<Evidence>

    @Query("SELECT * FROM Evidence WHERE uid  = :uid")
    fun getById(uid: Long): List<Evidence>

    @Query("Update Evidence SET status =:status, updatedat = :updatedat WHERE uid  =:uid")
    fun updateStatus(uid: Long, status: Boolean, updatedat: Long)

    @Query("SELECT * FROM Evidence WHERE uid = :uid LIMIT 1")
    fun loadAllById(uid: Long): Evidence

    @Insert
    fun insertAll(vararg users: Evidence)

    @Delete
    fun delete(checklist: Evidence)

    @Update
    fun updateAll(assetForm: Evidence)

    @Query("UPDATE Evidence SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""
    SELECT Evidence.* FROM Evidence
    INNER JOIN EventReport ON Evidence.event_report_id = EventReport.uid
    WHERE EventReport.status = 1
    """)
    suspend fun getUnsyncedData(): List<Evidence>

    @Query("SELECT * FROM Evidence WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): Evidence?
}