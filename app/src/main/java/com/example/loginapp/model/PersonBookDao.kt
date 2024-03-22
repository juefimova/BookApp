package com.example.loginapp.model

import androidx.room.*

@Dao
interface PersonBookDao {
    @Query("SELECT * FROM personbook")
    fun getAll(): List<PersonBook>

    @Insert
    fun insertBooks(vararg books: PersonBook)

    @Query("SELECT * FROM personbook WHERE user_id LIKE :user_id AND name LIKE :name LIMIT 1")
    fun getUserBooks(user_id: Long, name: String): List<PersonBook>

    @Query("DELETE FROM personbook WHERE user_id LIKE :user_id AND name LIKE :name")
    fun deleteBookIDuserName(user_id: Long, name: String)

    @Delete
    fun deleteBook(book: PersonBook)


}