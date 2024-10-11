package com.ssw.kast.model.persistence.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "song_table")
data class SongDao(
    @PrimaryKey val id: String,
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
    val date: String
)

@Dao
interface ISongDao {
    @Insert
    suspend fun insert(songDao: SongDao)

    @Query("SELECT * FROM song_table WHERE dataType = 2 LIMIT 15")
    suspend fun getRecentSongs(): List<SongDao>

    @Query("DELETE FROM song_table WHERE dataType = 2 AND id = (SELECT id FROM song_table ORDER BY date ASC LIMIT 1)")
    suspend fun deleteOldestRecentSong()

    @Query("DELETE FROM song_table WHERE dataType = 2 AND id = :songId")
    suspend fun deleteRecentSong(songId: String)

    @Query("DELETE FROM song_table WHERE dataType = 2 AND id NOT IN (SELECT id FROM song_table ORDER BY date DESC LIMIT 15)")
    suspend fun deleteRecentSongsAfterLimit()
}