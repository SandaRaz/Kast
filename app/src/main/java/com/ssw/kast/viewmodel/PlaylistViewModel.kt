package com.ssw.kast.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ssw.kast.model.component.MusicCardModel
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.dto.PlaylistDto
import com.ssw.kast.model.entity.Playlist
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.network.repository.PlaylistRepository
import com.ssw.kast.ui.screen.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
): ViewModel() {
    private val _playlistCreationError = mutableStateOf(ErrorCatcher())
    val playlistCreationError = _playlistCreationError

    private val _playlistError = mutableStateOf(ErrorCatcher())
    val playlistError: State<ErrorCatcher> = _playlistError

    private val _songSwapError = mutableStateOf(ErrorCatcher())
    val songSwapError: State<ErrorCatcher> = _songSwapError

    private val _playlist = mutableStateOf<Playlist?>(null)
    val playlist: State<Playlist?> = _playlist

    val playlists = mutableStateListOf<Playlist>()
    val playlistPickers = mutableStateListOf<PickerElement>()

    private val _playlistCards = mutableStateOf<List<MusicCardModel>>(emptyList())
    val playlistCard: State<List<MusicCardModel>> = _playlistCards

    suspend fun addSongToPlaylist(playlistId: Any, songId: Any) {
        try {
            _playlistError.value = playlistRepository.addSongToPlaylist(playlistId, songId)
        } catch (e: Exception) {
            Log.d("AddPlaylistSong", "Exception: ${e.message}")
        }
    }

    suspend fun createPlaylist(name: String, userId: Any): Playlist? {
        var playlist: Playlist? = null
        try {
            if (name.isBlank()) {
                throw Exception("Name should contains at least one character")
            }

            val playlistDto = PlaylistDto(
                id = UUID.randomUUID(),
                name = name,
                createdAt = LocalDateTime.now(),
                userId = userId
            )

            _playlistCreationError.value = playlistRepository.createPlaylist(playlistDto)
            if (_playlistCreationError.value.code == 0) {
                playlist = Playlist.getPlaylist(playlistDto)
            }
        } catch (e: Exception) {
            _playlistCreationError.value = ErrorCatcher("${e.message}", 1)

            Log.e("CreatePlaylist", "Exception: ${e.message}")
            e.printStackTrace()
        }

        return playlist
    }

    suspend fun deletePlaylist(playlistId: Any) {
        try {
            _playlistError.value = playlistRepository.deletePlaylist(playlistId)
        } catch (e: Exception) {
            Log.e("DeletePlaylist", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadPlaylist(playlistId: Any) {
        try {
            _playlist.value = playlistRepository.getPlaylist(playlistId)
        } catch (e: Exception) {
            Log.e("LoadPlaylist", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadPlaylistPickers(userId: Any) {
        try {
            if (playlistPickers.isEmpty()) {
                refreshPlaylistPickers(userId)
            }
        } catch (e: Exception) {
            Log.e("PlaylistPicker", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun refreshPlaylistPickers(userId: Any) {
        try {
            loadUserPlaylists(userId)
            playlists.forEach { playlist ->
                playlistPickers.add(
                    PickerElement(playlist.name, playlist.id)
                )
            }
        } catch (e: Exception) {
            Log.e("PlaylistPicker", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadUserPlaylists(userId: Any) {
        try {
            if (playlists.isEmpty()) {
                refreshUserPlaylists(userId)
            }
        } catch (e: Exception) {
            Log.e("DeletePlaylist", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun refreshUserPlaylists(userId: Any) {
        val databasePlaylists = playlistRepository.getPlaylists(userId)
        this.playlists.clear()

        databasePlaylists.forEach { playlist ->
            this.playlists.add(playlist)
        }
    }

    fun removePlaylistFromVMPlaylists(playlistId: Any) {
        for (playlist in playlists) {
            if (playlist.id == playlistId) {
                this.playlists.remove(playlist)
                break
            }
        }
    }

    fun removePlaylistPickerFromVMPlaylistPickers(playlistId: Any) {
        for (playlistPicker in playlistPickers) {
            if (playlistPicker.value == playlistId) {
                this.playlistPickers.remove(playlistPicker)
                break
            }
        }
    }

    suspend fun removeSongFromPlaylist(playlistId: Any, songId: Any) {
        try {
            _playlistError.value = playlistRepository.removeSongFromPlaylist(playlistId, songId)
        } catch (e: Exception) {
            Log.e("RemoveSong", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun renamePlaylist(userId: Any, playlistId: Any, newName: String) {
        try {
            if (newName.isBlank()) {
                throw Exception("Name should contains at least one character")
            }

            _playlistError.value = playlistRepository.renamePlaylist(userId, playlistId, newName)
            if (_playlistError.value.code == 0) {
                renamePlaylistFromVM(playlistId, newName)
            }
        } catch (e: Exception) {
            _playlistError.value = ErrorCatcher("${e.message}", 1)

            Log.e("RenamePlaylist", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun renamePlaylistFromVM(playlistId: Any, newName: String) {
        for (playlist in playlists) {
            if (playlist.id == playlistId) {
                playlist.name = newName
                playlists.remove(playlist)
                playlists.add(playlist)

                break
            }
        }
        for (playlistPicker in playlistPickers) {
            if (playlistPicker.value == playlistId) {
                playlistPicker.label = newName
                playlistPickers.remove(playlistPicker)
                playlistPickers.add(playlistPicker)

                break;
            }
        }
    }

    suspend fun loadPlaylistCards(
        navController: NavHostController,
        userId: Any,
        amount: Int,
        defaultCover: ImageBitmap
    ) {
        try {
            if (_playlistCards.value.isEmpty()) {
                if (playlists.isEmpty()) {
                    loadUserPlaylists(userId)
                }
                refreshPlaylistCards(navController, amount, defaultCover)
            }
        } catch (e: Exception) {
            Log.e("PlaylistCards", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun refreshPlaylistCards(
        navController: NavHostController,
        amount: Int,
        defaultCover: ImageBitmap
    ) {
        try {
            val playlistToShow = mutableListOf<MusicCardModel>()
            playlists.take(amount).forEach { playlist ->
                playlistToShow.add(
                    MusicCardModel(
                        title = playlist.name,
                        image = defaultCover,
                        onClick = {
                            viewModelScope.launch {
                                loadPlaylist(playlist.id)
                                NavigationManager.navigateTo(navController, "playlist_music")
                            }
                        }
                    )
                )
            }

            this._playlistCards.value = playlistToShow
        } catch (e: Exception) {
            Log.e("PlaylistCards", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun moveSongToUpper(playlist: Playlist, songId: Any) {
        _songSwapError.value = playlist.moveSongToUpper(playlistRepository, songId)
    }

    suspend fun moveSongToLower(playlist: Playlist, songId: Any) {
        _songSwapError.value = playlist.moveSongToLower(playlistRepository, songId)
    }
}