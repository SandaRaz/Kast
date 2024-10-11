package com.ssw.kast.model.persistence.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "user_table")
data class UserDao (
    @PrimaryKey val id: String,
    val username: String,
    val usermail: String,
    val passwordHash: String,
    val dateOfBirth: String,
    val profilePictureCode: String,
    val biography: String,
    val followersCount: Int,
    val followingCount: Int,
    val createdAt: String,
    val state: Int,
    val genderId: String,
    val genderType: String,
    val countryId: String,
    val countryName: String,
    val statusId: String,
    val statusLabel: String,
    val statusRank: Int
)

@Dao
interface IUserDao {
    @Query("SELECT COUNT(*) FROM user_table")
    suspend fun countUsers(): Int

    @Insert
    suspend fun insert(userDao: UserDao)

    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getLatestUser(): UserDao?

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers(): List<UserDao>

    @Update
    suspend fun updateUser(userDao: UserDao)

    // *** THIS Function does not return correct Int value
    @Query("DELETE FROM user_table")
    suspend fun emptyUsers(): Int

    @Query("UPDATE user_table SET followersCount = followersCount+1 WHERE id = :userId")
    suspend fun increaseFollower(userId: String)

    @Query("UPDATE user_table SET followersCount = followersCount-1 WHERE id = :userId")
    suspend fun decreaseFollower(userId: String)

    @Query("UPDATE user_table SET followingCount = followingCount+1 WHERE id = :userId")
    suspend fun increaseFollowing(userId: String)

    @Query("UPDATE user_table SET followingCount = followingCount-1 WHERE id = :userId")
    suspend fun decreaseFollowing(userId: String)
}