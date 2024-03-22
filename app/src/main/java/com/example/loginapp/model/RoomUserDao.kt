package com.example.loginapp.model
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomUserDao {
    @Query("SELECT * FROM roomuser1")
    fun getAll(): List<RoomUser1>

    @Query("SELECT * FROM roomuser1 WHERE id = :userID")
    fun loadAllByID(userID: Long): RoomUser1

    @Query("SELECT * FROM roomuser1 WHERE email LIKE :first AND password LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): RoomUser1

    @Query("UPDATE roomuser1 SET email = :email, password = :password WHERE id = :currentId")
    fun update(email: String?, password: String?, currentId: Long)

    @Insert
    fun insertAll(vararg users: RoomUser1)

    @Delete
    fun delete(user: RoomUser1)
}