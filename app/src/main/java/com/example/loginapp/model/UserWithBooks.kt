package com.example.loginapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
class UserWithBooks (
        @Embedded
        val user: RoomUser1,
        @Relation(
                parentColumn = "id",
                entityColumn = "user_id"
        )
        val books: List<PersonBook>
        )
