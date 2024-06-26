package com.example.loginapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomBook (
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "description")
        val description: String,
        @ColumnInfo(name = "image")
        val image: Int,
        @ColumnInfo(name = "rating")
        val rating: Float,
        @ColumnInfo(name="usersClicked")
        val usersClicked: Int
): java.io.Serializable