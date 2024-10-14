package com.ssw.kast.model.entity

import android.util.Log
import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.dto.PlaylistDto
import com.ssw.kast.network.repository.PlaylistRepository
import org.threeten.bp.LocalDateTime

class Playlist {
    var id: Any = ""
    var name: String = ""
    var createdAt: LocalDateTime = LocalDateTime.now()
    var userId: Any = ""
    var user: User = User()
    var songs: MutableList<Song> = mutableListOf()

    constructor()

    constructor(id: Any, name: String, createdAt: LocalDateTime, userId: Any) {
        this.id = id
        this.name = name
        this.createdAt = createdAt
        this.userId = userId
    }

    suspend fun swap2songs(
        playlistRepository: PlaylistRepository,
        song1Id: Any,
        song2Id: Any
    ): ErrorCatcher {

        var err = ErrorCatcher()
        try {
            val index1 = songs.indexOfFirst { it.id == song1Id }
            val index2 = songs.indexOfFirst { it.id == song2Id }
            if (index1 != -1 && index2 != -1) {
                songs[index1] = songs[index2].also {
                    songs[index2] = songs[index1]
                }

                err = playlistRepository.swapPlaylistSongs(this.id, song1Id, song2Id)
            }
        } catch (e: Exception) {
            err.error = "${e.message}"
            err.code = 1

            Log.e("SwapPlaylistSongs","Exception: ${e.message}")
            e.printStackTrace()
        }
        return err
    }

    suspend fun moveSongToUpper(
        playlistRepository: PlaylistRepository,
        songId: Any,
    ): ErrorCatcher {
        var err = ErrorCatcher()
        try {
            val songIndex = songs.indexOfFirst { it.id == songId }
            if (songIndex > 0) {
                val song2Index = songIndex - 1
                err = swap2songs(playlistRepository, songId, songs[song2Index].id)
            }
        } catch (e: Exception) {
            err.error = "${e.message}"
            err.code = 1

            Log.e("SwapPlaylistSongs","Exception: ${e.message}")
            e.printStackTrace()
        }

        return err
    }

    suspend fun moveSongToLower(
        playlistRepository: PlaylistRepository,
        songId: Any
    ): ErrorCatcher {
        var err = ErrorCatcher()
        try {
            val songIndex = songs.indexOfFirst { it.id == songId }
            if (songIndex >= 0 && songIndex < songs.size-1) {
                val song2Index = songIndex + 1
                err = swap2songs(playlistRepository, songId, songs[song2Index].id)
            }
        } catch (e: Exception) {
            err.error = "${e.message}"
            err.code = 1

            Log.e("SwapPlaylistSongs","Exception: ${e.message}")
            e.printStackTrace()
        }
        return err
    }

    companion object {
        fun getPlaylist(playlistDto: PlaylistDto): Playlist {
            val playlist = Playlist(
                id = playlistDto.id,
                name = playlistDto.name,
                createdAt = playlistDto.createdAt,
                userId = playlistDto.userId
            )

            return playlist
        }
    }
}