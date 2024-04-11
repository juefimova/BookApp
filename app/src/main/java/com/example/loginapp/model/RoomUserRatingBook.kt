package com.example.loginapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomUserRatingBook (
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "rating")
        val rating: Float
        ): java.io.Serializable