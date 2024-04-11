package com.example.loginapp.model

import androidx.room.*

@Dao
interface RoomUserRatingBookDao {
    @Query("SELECT * FROM roomuserratingbook")
    fun getAll(): List<RoomUserRatingBook>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook( book: RoomUserRatingBook)

    @Delete
    fun deleteBook(book: RoomUserRatingBook)
}