package com.ssw.kast.network.repository

import android.util.Log
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.view.TopCategory
import com.ssw.kast.network.service.SongService
import javax.inject.Inject

class SongRepository @Inject constructor(private val songService: SongService) {
    @Deprecated("It use songService.getAudioStream's deprecated api")
    suspend fun getAudioStream(songId: Any): String? {
        Log.d("SongRepository", "Fetch getAudioStream Response")
        val response = songService.getAudioStream(songId)
        Log.d("SongRepository","Response: ${response.body()}")

        return if (response.isSuccessful) {
            val hlsContent = response.body()?.string()
            Log.d("HLS-Content", hlsContent ?: "Empty content")
            hlsContent?.let {
                val lines = it.lines()
                lines.find { line ->
                    line.endsWith(".ts")
                }
            }
        } else {
            Log.e("HLS-Stream","Failed to get HLS stream: ${response.code()}")
            null
        }
    }

    suspend fun getMusicGenreSongs(
        userId: Any,
        musicGenreId: Any,
        offset: Int,
        limit: Int
    ): List<Song> = songService.getMusicGenreSongs(userId, musicGenreId, offset, limit)

    suspend fun getSuggestedSongs(userId: Any): List<Song> = songService.getSuggestedSongs(userId)

    suspend fun getTopCategories(): List<TopCategory> = songService.getTopCategories()

    suspend fun mostStreamedSongs(amount: Int): List<Song> = songService.mostStreamedSongs(amount)
}