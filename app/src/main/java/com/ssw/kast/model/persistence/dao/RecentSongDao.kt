package com.ssw.kast.model.persistence.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "recentsong_table")
data class RecentSongDao(
    @PrimaryKey val id: String,
    val songId: String,
    val title: String,
    val singer: String,
    val year: Int,
    val duration: String,
    val localPath: String,
    val coverCode: String,
    val albumId: String,
    val albumName: String,
    val musicGenreId: String,
    val musicGenreType: String,
    val uploaderId: String,
    val state: Int,
    val uploadAt: String,
    val listeners: Int,
    val dataType: Int, /* exemple: recent = 2 */
    val date: String,
    val userId: String
)

@Dao
interface IRecentSongDao {
    @Insert
    suspend fun insert(recentSongDao: RecentSongDao)

    @Query("SELECT * FROM recentsong_table WHERE userId = :userId AND dataType = 2 LIMIT 15")
    suspend fun getRecentSongs(userId: String): List<RecentSongDao>

    @Query("DELETE FROM recentsong_table WHERE dataType = 2 AND id = (SELECT id FROM recentsong_table WHERE userId = :userId ORDER BY date ASC LIMIT 1)")
    suspend fun deleteOldestRecentSong(userId: String)

    @Query("DELETE FROM recentsong_table WHERE dataType = 2 AND songId = :songId AND userId = :userId")
    suspend fun deleteRecentSong(songId: String, userId: String)

    @Query("DELETE FROM recentsong_table WHERE dataType = 2 AND id NOT IN (SELECT id FROM recentsong_table WHERE userId = :userId ORDER BY date DESC LIMIT 15)")
    suspend fun deleteRecentSongsAfterLimit(userId: String)
}