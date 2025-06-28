package com.csi.irite.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csi.irite.room.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE uid = :id")
    suspend fun getUser(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getLoggedInUser(): User?
}