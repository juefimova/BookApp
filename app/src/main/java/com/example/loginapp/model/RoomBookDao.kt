package com.example.loginapp.model

import android.icu.text.CaseMap.Title
import androidx.room.*

@Dao
interface RoomBookDao {
    @Query("SELECT * FROM roombook")
    fun getAll(): List<RoomBook>


    @Query("UPDATE roombook SET rating = :rating, usersClicked = :usersClicked WHERE id = :currentId")
    fun update(rating: Float, usersClicked: Int, currentId: Long)

    @Query("SELECT rating FROM roombook WHERE id LIKE :currentId AND title LIKE :title LIMIT 1")
    fun selectRating(currentId: Long, title: String) : Float

    @Query("SELECT usersClicked FROM roombook WHERE id LIKE :currentId AND title LIKE :title LIMIT 1")
    fun selectUsersClicked(currentId: Long, title: String) : Int



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(vararg books: RoomBook)

    @Delete
    fun deleteBook(book: RoomBook)
}