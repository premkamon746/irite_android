package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.csi.irite.room.data.BaseEntity
import com.csi.irite.room.data.CheckListHead
import com.csi.irite.room.data.EventReport

@Dao
interface CheckListHeadDao {
    @Query("SELECT * FROM CheckListHead")
    fun getAll(): List<CheckListHead>

    @Query("SELECT * FROM CheckListHead WHERE uid IN (:uid) and status = :status")
    fun findActiveAllByIds(uid: IntArray, status: Boolean): MutableList<CheckListHead>

    @Query("SELECT * FROM CheckListHead WHERE uid IN (:userIds)")
    fun findAllByIds(userIds: IntArray): MutableList<CheckListHead>

    @Query("SELECT * FROM CheckListHead WHERE uid  = :uid LIMIT 1")
    fun getById(uid: Long): CheckListHead

    @Query("Update CheckListHead SET status =:status, updatedat = :updatedat WHERE uid  =:uid")
    fun updateStatus(uid: Long, status: Boolean, updatedat: Long)


    @Insert
    fun insert(checkListHead: CheckListHead):Long

    @Delete
    fun delete(checkListHead: CheckListHead)

    @Update
    fun update(checkListHead: CheckListHead)


    @Query("SELECT COUNT(*) FROM CheckListHead WHERE datetime BETWEEN :startOfDay AND :endOfDay AND team = :team")
    fun countEntriesForTodayAndTeam(startOfDay: Long, endOfDay: Long, team: String): Int


    @Query("SELECT uid FROM CheckListHead WHERE datetime BETWEEN :startOfDay AND :endOfDay AND team = :team  LIMIT 1")
    fun getHeadID(startOfDay: Long, endOfDay: Long, team: String): Long

    @Query("UPDATE CheckListHead SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""SELECT CheckListHead.* FROM CheckListHead""")
    suspend fun getUnsyncedData(): List<CheckListHead>

    @Query("SELECT * FROM CheckListHead WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): CheckListHead?

    @Insert
    fun insertAll(events: CheckListHead): Long

    @Update
    fun updateAll(events: CheckListHead)

}