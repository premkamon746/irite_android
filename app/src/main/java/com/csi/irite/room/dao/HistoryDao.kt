package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csi.irite.room.data.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History WHERE uid = :id")
    suspend fun getHistory(id: Int): History?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Query("UPDATE History SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("SELECT * FROM History")
    suspend fun getUnsyncedData(): List<History>

    @Query("SELECT * FROM History WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): History?

}