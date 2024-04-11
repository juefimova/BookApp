package com.example.loginapp.model

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(entities = [RoomUser1::class, RoomBook::class,
    PersonBook::class, RoomUserFriends::class, RoomPhone::class,
    RoomUserRatingBook::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): RoomUserDao

    abstract fun bookDao(): RoomBookDao

    abstract fun userBookDao(): UserBookDao

    abstract fun personBookDao(): PersonBookDao

    abstract fun userFriendsDao(): RoomUserFriendsDao

    abstract fun roomPhoneDao(): RoomPhoneDao

    abstract fun roomUserRatingBookDao(): RoomUserRatingBookDao

}