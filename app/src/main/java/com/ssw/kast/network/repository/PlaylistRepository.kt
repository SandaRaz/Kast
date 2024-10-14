package com.ssw.kast.network.repository

import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.dto.PlaylistDto
import com.ssw.kast.model.entity.Playlist
import com.ssw.kast.network.service.PlaylistService
import javax.inject.Inject

class PlaylistRepository @Inject constructor(private val playlistService: PlaylistService) {
    suspend fun addSongToPlaylist(playlistId: Any, songId: Any): ErrorCatcher = playlistService.addSongToPlaylist(playlistId, songId)
    suspend fun createPlaylist(playlist: PlaylistDto): ErrorCatcher = playlistService.createPlaylist(playlist)
    suspend fun deletePlaylist(playlistId: Any): ErrorCatcher = playlistService.deletePlaylist(playlistId)
    suspend fun getPlaylist(playlistId: Any): Playlist = playlistService.getPlaylist(playlistId)
    suspend fun getPlaylists(userId: Any): List<Playlist> = playlistService.getPlaylists(userId)
    suspend fun removeSongFromPlaylist(playlistId: Any, songId: Any): ErrorCatcher = playlistService.removeSongFromPlaylist(playlistId, songId)
    suspend fun renamePlaylist(userId: Any, playlistId: Any, newName: String): ErrorCatcher = playlistService.renamePlaylist(userId, playlistId, newName)
    suspend fun swapPlaylistSongs(playlistId: Any, song1Id: Any, song2Id: Any): ErrorCatcher = playlistService.swapPlaylistSongs(playlistId, song1Id, song2Id)
}