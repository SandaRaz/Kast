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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.component.AddToPlaylistPopup
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.CurrentSongBar
import com.ssw.kast.ui.component.MusicListCard
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch

@Composable
fun RecentScreen (
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    accountManager: AccountManager,
    songManager: SongManager,
    playlistViewModel: PlaylistViewModel
) {
    // ---------- Retrieve data from server ----------

    val scope = rememberCoroutineScope()

    var loggedUser by remember { mutableStateOf(User()) }
    var recentPlayed by remember { mutableStateOf<List<Song>>(emptyList()) }

    LaunchedEffect(Unit) {
        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!

            songManager.loadRecentSong(loggedUser.id)
            recentPlayed = songManager.recentSongs
            recentPlayed = recentPlayed.reversed()
        } else {
            NavigationManager.navigateTo(navController,"sign_in")
        }
    }

    val showAddToPlaylistDialog = remember { mutableStateOf(false) }
    val songToAddToPlaylist = remember { mutableStateOf<Song?>(null) }

    /*
    SideEffect {
        reloadTrigger.intValue += 1
        Log.d("Suggestions","ReloadTrigger VALUE => ${reloadTrigger.intValue}")
    }
    */

    // -----------------------------------------------

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
            BackNavBar(
                title = "Recent played (${songManager.recentSongs.size})",
                onPressBackButton = {
                    NavigationManager.goToPreviousScreen(
                        navController = navController,
                        selectedItem = selectedItem,
                        onPreviousRouteNull = {
                            NavigationManager.navigateTo(navController,"music")
                        }
                    )
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
                items(recentPlayed) { song ->
                    MusicListCard(
                        song = song,
                        songManager = songManager,
                        onClick = {
                            scope.launch {
                                try {
                                    songManager.onSongListChange(recentPlayed)
                                    songManager.clickNewSong(song, loggedUser.id, true)
                                    navController.navigate("player")
                                } catch (e: Exception) {
                                    Log.e("Recent", "Exception: ${e.message}")
                                    e.printStackTrace()
                                }
                            }
                        },
                        extraIcon2 = Icons.Outlined.MoreVert,
                        onClickExtraIcon2 = {
                            songToAddToPlaylist.value = song
                            showAddToPlaylistDialog.value = true
                        }
                    )
                }
            }

            AddToPlaylistPopup(
                isShowed = showAddToPlaylistDialog,
                song = songToAddToPlaylist,
                userId = loggedUser.id,
                playlistViewModel = playlistViewModel
            )

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