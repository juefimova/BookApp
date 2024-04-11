package com.example.loginapp.model;

import androidx.room.*;

@Dao
 interface RoomUserFriendsDao {

 @Insert
 fun insertFriend(friend: RoomUserFriends)

 @Query("SELECT * FROM roomUserFriends WHERE name LIKE :name AND number LIKE :number LIMIT 1")
 fun findByName(name: String, number: String): RoomUserFriends

 @Query("SELECT * FROM roomuserfriends")
 fun getAll(): List<RoomUserFriends>

 @Delete
 fun deleteFriend(friend: RoomUserFriends)

}
