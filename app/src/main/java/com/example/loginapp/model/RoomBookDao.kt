package com.example.loginapp.model

import androidx.room.*

@Dao
interface RoomBookDao {
    @Query("SELECT * FROM roombook")
    fun getAll(): List<RoomBook>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(vararg books: RoomBook)

    @Delete
    fun deleteBook(book: RoomBook)
}