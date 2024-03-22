package com.example.loginapp.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserBookDao {
    @Transaction
    @Query("SELECT * FROM RoomUser1")
    fun getUserWithBooks(): List<UserWithBooks>
}