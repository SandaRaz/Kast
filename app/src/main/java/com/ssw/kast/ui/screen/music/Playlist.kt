package com.ssw.kast.ui.screen.music

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssw.kast.R
import com.ssw.kast.model.entity.Playlist
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.component.CreateNewPlaylistPopup
import com.ssw.kast.ui.component.CurrentSongBar
import com.ssw.kast.ui.component.ListCard
import com.ssw.kast.ui.component.PlaylistNavBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.component.YesOrNoPopup
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch

@Composable
fun PlaylistScreen (
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    accountManager: AccountManager,
    songManager: SongManager,
    playlistViewModel: PlaylistViewModel
) {
    // ---------- Fetching data server ----------
    val scope = rememberCoroutineScope()

    var loggedUser by remember { mutableStateOf(User()) }

    val defaultPlaylistCover = getCachedImageFromResources(resourceId = R.drawable.default_playlist_cover)

    LaunchedEffect(Unit) {
        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!

            playlistViewModel.loadUserPlaylists(loggedUser.id)
        } else {
            NavigationManager.navigateTo(navController,"sign_in")
        }
    }
    val countPlaylists = remember { mutableIntStateOf(playlistViewModel.playlists.size) }

    val showCreatePlaylistDialog = remember { mutableStateOf(false) }

    val showDeletePlaylistDialog = remember { mutableStateOf(false) }
    var playlistToDelete by remember { mutableStateOf<Playlist?>(null) }
    var playlistDeleteError by remember { mutableStateOf("") }

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
                title = "Playlist (${countPlaylists.intValue})",
                underline = true,
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
                        playlistViewModel.refreshUserPlaylists(loggedUser.id)
                    }
                },
                extraIcon = Icons.Outlined.Add,
                onClickExtraIcon = {
                    showCreatePlaylistDialog.value = true
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
                items(playlistViewModel.playlists) { playlist ->
                    Row (
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ListCard(
                            title = playlist.name,
                            image = defaultPlaylistCover,
                            upperLine = false,
                            underLine = false,
                            onClick = {
                                scope.launch {
                                    playlistViewModel.loadPlaylist(playlist.id)
                                    NavigationManager.navigateTo(navController, "playlist_music")
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                        )

                        Icon (
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "Delete playlist",
                            modifier = Modifier
                                .clickable {
                                    playlistToDelete = playlist
                                    showDeletePlaylistDialog.value = true
                                }
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            CreateNewPlaylistPopup(
                isShowed = showCreatePlaylistDialog,
                creatorId = loggedUser.id,
                countPlaylist = countPlaylists,
                playlistViewModel = playlistViewModel
            )

            playlistToDelete?.let { playlist ->
                YesOrNoPopup(
                    isShowed = showDeletePlaylistDialog,
                    title = "Are you sure you want to delete '${playlist.name}' from playlist",
                    onClickYes = {
                        scope.launch {
                            playlistViewModel.deletePlaylist(playlist.id)
                            if (playlistViewModel.playlistError.value.code != 0) {
                                playlistDeleteError = playlistViewModel.playlistError.value.error
                            } else {
                                playlistViewModel.removePlaylistFromVMPlaylists(playlist.id)
                                playlistViewModel.removePlaylistPickerFromVMPlaylistPickers(playlist.id)
                                playlistViewModel.refreshPlaylistCards(navController, 5, defaultPlaylistCover)

                                countPlaylists.intValue = playlistViewModel.playlists.size
                                showDeletePlaylistDialog.value = false
                            }
                        }
                    },
                    onClickNo = {
                        playlistToDelete = null
                    },
                    error = {
                        if (playlistDeleteError.isNotBlank()) {
                            Text (
                                text = playlistDeleteError,
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