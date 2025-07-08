package org.mapsforge.map.model.com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.Evidence
import com.csi.irite.room.data.EvidentBag

@Dao
interface EvidentBagDao {
    @Query("SELECT * FROM EvidentBag")
    fun getAll(): MutableList<EvidentBag>

    @Query("SELECT * FROM EvidentBag WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<EvidentBag>

    @Query("SELECT * FROM EvidentBag WHERE event_report_id  = :event_report_id")
    fun getByIds(event_report_id: Long): List<EvidentBag>

    @Query("SELECT evident_id FROM EvidentBag WHERE evident_id  = :evident_id LIMIT 1" )
    fun getByEvidentIds(evident_id: Long): Long

    @Query("SELECT * FROM EvidentBag WHERE event_report_id = :event_report_id")
    fun findEventId(event_report_id: Long):  List<EvidentBag>


    @Query("SELECT max(seq_no) FROM EvidentBag WHERE event_report_id IN (:uid) LIMIT 1")
    fun  getLastSeqNO(uid:Long):Int

    @Query("SELECT * FROM EvidentBag WHERE uid  = :uid")
    fun getById(uid: Long): MutableList<EvidentBag>

    @Query("SELECT * FROM EvidentBag WHERE uid = :uid LIMIT 1")
    fun loadAllById(uid: Long): EvidentBag


    @Query("Update EvidentBag SET paper =:status , updatedat = :updatedat WHERE uid  =:uid")
    fun updatePaper(uid: Long, status:Boolean, updatedat:Long)

    @Query("Update EvidentBag SET plastic =:status, updatedat = :updatedat WHERE uid  =:uid")
    fun updatePlastic(uid: Long, status:Boolean, updatedat:Long)

    @Query("Update EvidentBag SET return_pks =:value , updatedat = :updatedat WHERE uid  =:uid")
    fun updatePsk(uid: Long, value:String, updatedat:Long)

    @Query("Update EvidentBag SET number =:value , updatedat = :updatedat WHERE uid  =:uid")
    fun updateNumber(uid: Long, value:String, updatedat:Long)

    @Query("Update EvidentBag SET place =:value , updatedat = :updatedat WHERE uid  =:uid")
    fun updatePlace(uid: Long, value:String, updatedat:Long)

    @Query("Update EvidentBag SET tag_no =:value , updatedat = :updatedat WHERE uid  =:uid")
    fun updateTagno(uid: Long, value:String, updatedat:Long)

    @RawQuery
    fun updateField(query: SupportSQLiteQuery): Int

    @Insert
    fun insertAll(vararg users: EvidentBag)

    @Delete
    fun delete(checklist: EvidentBag)

    @Update
    fun updateAll(assetForm: EvidentBag)

    @Query("UPDATE EvidentBag SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""
    SELECT EvidentBag.* FROM EvidentBag
    INNER JOIN EventReport ON EvidentBag.event_report_id = EventReport.uid
    WHERE status = 1
    """)
    suspend fun getUnsyncedData(): List<EvidentBag>

    @Query("SELECT * FROM EvidentBag WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): EvidentBag?
}