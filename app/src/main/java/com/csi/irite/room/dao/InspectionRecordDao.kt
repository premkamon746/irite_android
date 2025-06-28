package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.InspectionRecord

@Dao
interface InspectionRecordDao {
    @Insert
    fun insertAll(events: InspectionRecord): Long

    @Update
    fun updateAll(events: InspectionRecord)

    @RawQuery
    fun updateField(query: SupportSQLiteQuery): Int

    @Query("SELECT * FROM InspectionRecord")
    fun getAllChecklist(): List<InspectionRecord>

    @Query("SELECT * FROM InspectionRecord WHERE create_date = :create_date AND team = :team LIMIT 1")
    fun getDateTeam(create_date: String, team: String): InspectionRecord

    @Query("SELECT * FROM InspectionRecord WHERE uid = :uid LIMIT 1")
    fun getById(uid: Long?): InspectionRecord

    @Query("UPDATE InspectionRecord SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""SELECT * FROM InspectionRecord""")
    suspend fun getUnsyncedData(): List<InspectionRecord>

    @Query("SELECT * FROM InspectionRecord WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): InspectionRecord?

    /*@Query("SELECT COUNT(*) FROM InspectionRecord WHERE create_date = :create_date AND team = :team")
    fun countEntriesForTodayAndTeam(create_date: String, team: String): Int*/

    @Query("SELECT uid FROM InspectionRecord WHERE create_date = :create_date AND team = :team LIMIT 1")
    fun getIdIfExists(create_date: String, team: String): Long?
}