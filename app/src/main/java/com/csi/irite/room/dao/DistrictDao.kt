package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.csi.irite.room.data.District
import com.csi.irite.room.data.EventReport

@Dao
interface DistrictDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(districts: List<District>)

    @Query("SELECT * FROM District")
    fun getAllDistricts(): List<District>

    @Query("SELECT * FROM District where province = :province")
    fun getlDistrictsByProvince(province:String): List<District>

    @Insert
    fun insertAll(events: District): Long

    @Update
    fun updateAll(events: District)

    @Query("UPDATE District SET issynced = 1 WHERE uid IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("""SELECT District.* FROM District""")
    suspend fun getUnsyncedData(): List<District>

    @Query("SELECT * FROM District WHERE refKey = :refKey LIMIT 1")
    suspend fun findByRefKey(refKey: String): District?
}