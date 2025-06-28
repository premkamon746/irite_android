package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.csi.irite.room.data.EventReport

@Dao
interface EventReportDao {
    @Query("SELECT * FROM EventReport")
    fun getAll(): List<EventReport>

    @Query("SELECT * FROM EventReport where status = 1 ")
    fun getAllActiveStatus(): List<EventReport>

    @Query("SELECT * FROM EventReport WHERE uid = (:uid)")
    fun loadAllByIds(uid: Long): EventReport

    @Query("SELECT * FROM EventReport WHERE uid  = :uid")
    fun getById(uid: Long): List<EventReport>

    @Query("SELECT * FROM EventReport WHERE uid  = :uid and status = true ")
    fun getActiveById(uid: Long): List<EventReport>

    @Query("Update EventReport SET status =:status, updatedat = :updatedat WHERE uid  =:uid")
    fun updateStatus(uid: Long, status: Boolean, updatedat: Long)

    @Insert
    fun insertAll(events: EventReport): Long

    @Update
    fun updateAll(events: EventReport)

    @Delete
    fun delete(checklist: EventReport)

    @Query("UPDATE EventReport SET draw_map = :newImageData , updatedat = :updatedat WHERE uid = :id")
    fun updateDrawMapById(id: Long, newImageData: ByteArray, updatedat: Long)

    @Query("UPDATE EventReport SET draw_evidence = :newImageData , updatedat = :updatedat WHERE uid = :id")
    fun updateDrawEvidenceById(id: Long, newImageData: ByteArray, updatedat: Long)

    @Query("SELECT draw_map FROM EventReport WHERE uid = :id")
    fun getImageMapById(id: Long): ByteArray

    @Query("SELECT draw_evidence FROM EventReport WHERE uid = :id")
    fun getImageEvidenceById(id: Long): ByteArray

    @Query("UPDATE EventReport SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("UPDATE EventReport SET lat = :lat, lng = :lng, updatedAt = :updatedat WHERE uid = :id")
    abstract fun updateLocation(lat: Double, lng: Double, updatedat: Long, id: Long)

    @Query("SELECT * FROM EventReport WHERE status = 1")
    suspend fun getUnsyncedData(): List<EventReport>

    @Query("SELECT * FROM EventReport WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): EventReport?
}