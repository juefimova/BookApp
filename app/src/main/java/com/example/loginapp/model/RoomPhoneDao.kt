package com.example.loginapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomPhoneDao {
    @Insert
    fun insertPhones(phones: Array<RoomPhone>)

    @Query("SELECT * FROM roomphone WHERE phone LIKE :phone AND address LIKE :address LIMIT 1")
    fun findByName(phone: String, address: String): RoomPhone

    @Query("SELECT * FROM roomphone")
    fun getAll(): List<RoomPhone>

    @Delete
    fun deletePhone(phone: RoomPhone)
}