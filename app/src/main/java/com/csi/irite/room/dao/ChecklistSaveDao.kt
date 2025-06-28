package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.csi.irite.room.data.ChecklistSave
import com.csi.irite.room.data.EventReport

@Dao
interface ChecklistSaveDao {
    @Query("SELECT * FROM ChecklistSave")
    fun getAll(): List<ChecklistSave>

    @Query("SELECT * FROM ChecklistSave WHERE uid IN (:uid)")
    fun loadAllByIds(uid: IntArray): List<ChecklistSave>


    @Query("SELECT * FROM ChecklistSave WHERE create_datetime BETWEEN :startOfDay AND :endOfDay  AND gropping = :group")
    fun findGroup(startOfDay: Long, endOfDay: Long,  group: String):  List<ChecklistSave>

    @Insert
    fun insertAll(vararg users: ChecklistSave)

    @Delete
    fun delete(checklist: ChecklistSave)

    @Query("UPDATE ChecklistSave SET status = false WHERE checklist_head_id = :hid")
    fun setAllFalse(hid:Long)

    @Insert
    fun insertAll(events: ChecklistSave): Long

    @Update
    fun updateAll(events: ChecklistSave)

    @Query("UPDATE ChecklistSave SET status = true , updatedat = :updatedat WHERE uid  =:uid")
    fun updateStatus(uid: Long, updatedat: Long)

    @Query("UPDATE ChecklistSave SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""SELECT ChecklistSave.* FROM ChecklistSave""")
    suspend fun getUnsyncedData(): List<ChecklistSave>

    @Query("SELECT * FROM ChecklistSave WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): ChecklistSave?

}