package com.ssw.kast.ui.screen.music

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssw.kast.model.entity.Playlist
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.component.CurrentSongBar
import com.ssw.kast.ui.component.MusicListCard
import com.ssw.kast.ui.component.PlaylistNavBar
import com.ssw.kast.ui.component.RenamePlaylistPopup
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.component.YesOrNoPopup
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch


@Composable
fun PlaylistMusicScreen (
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    accountManager: AccountManager,
    songManager: SongManager,
    playlistViewModel: PlaylistViewModel
) {
    // ---------- Fetching data server ----------
    val scope = rememberCoroutineScope()

    var loggedUser by remember { mutableStateOf(User()) }
    var playlist by remember { mutableStateOf(Playlist()) }
    val playlistName = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!

            if (playlistViewModel.playlist.value == null) {
                NavigationManager.navigateTo(navController,"playlist")
            } else {
                playlist = playlistViewModel.playlist.value!!
                playlistName.value = playlist.name
            }
        } else {
            NavigationManager.navigateTo(navController,"sign_in")
        }
    }

    val showRemoveDialog = remember { mutableStateOf(false) }
    var songToRemove by remember { mutableStateOf<Song?>(null) }
    var removeSongError by remember { mutableStateOf("") }

    val showRenameDialog = remember { mutableStateOf(false) }

    // ------------------------------------------

    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            PlaylistNavBar(
                title = "${playlistName.value} (${playlist.songs.size})",
                onPressBackButton = {
                    NavigationManager.goToPreviousScreen(
                        navController,
                        selectedItem,
                        onPreviousRouteNull = {
                            NavigationManager.navigateTo(navController,"music")
                        }
                    )
                },
                onClickRefreshButton = {
                    scope.launch {
                        playlistViewModel.loadPlaylist(playlist.id)
                        if (playlistViewModel.playlist.value == null) {
                            NavigationManager.navigateTo(navController,"playlist")
                        } else {
                            playlist = playlistViewModel.playlist.value!!
                        }
                    }
                },
                extraIcon = Icons.Outlined.Edit,
                onClickExtraIcon = {
                    showRenameDialog.value = true
                }
            )

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
            ) {
                items(playlist.songs) { song ->
                    song.loadCoverBitmapAndEmptyCoverCode()

                    MusicListCard(
                        song = song,
                        songManager = songManager,
                        onClick = {
                            scope.launch {
                                try {
                                    songManager.onSongListChange(playlist.songs)
                                    songManager.clickNewSong(song, loggedUser.id, true)
                                    NavigationManager.navigateTo(navController,"player")
                                } catch (e: Exception) {
                                    Log.e("Suggestions", "Exception: ${e.message}")
                                    e.printStackTrace()
                                }
                            }
                        },
                        extraIcon = Icons.Outlined.Delete,
                        onClickExtraIcon = {
                            songToRemove = song
                            showRemoveDialog.value = true
                        }
                    )
                }
            }

            RenamePlaylistPopup(
                isShowed = showRenameDialog,
                userId = loggedUser.id,
                playlistId = playlist.id,
                playlistName = playlistName,
                playlistViewModel = playlistViewModel,
                onRenameSuccess = { newName ->
                    playlist.name = newName
                    playlistName.value = newName
                }
            )

            songToRemove?.let {
                YesOrNoPopup(
                    isShowed = showRemoveDialog,
                    title = "Remove ${it.title} from ${playlist.name} ?",
                    onClickYes = {
                        scope.launch {
                            playlistViewModel.removeSongFromPlaylist(playlist.id, it.id)
                            if (playlistViewModel.playlistError.value.code != 0) {
                                removeSongError = playlistViewModel.playlistError.value.error
                            } else {
                                playlistViewModel.loadPlaylist(playlist.id)
                                playlist = playlistViewModel.playlist.value!!

                                showRemoveDialog.value = false
                            }
                        }
                    },
                    onClickNo = {
                        songToRemove = null
                    },
                    error = {
                        if (removeSongError.isNotBlank()) {
                            Text (
                                text = removeSongError,
                                color = Color.Red
                            )
                        }
                    }
                )
            }

            songManager.currentSong?.let {
                CurrentSongBar(
                    navController = navController,
                    songManager = songManager,
                    onClickPlay = {
                        songManager.clickCurrentSong()
                    }
                )
            }
        }
    }
}