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
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
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
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.MusicListCard
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.component.AddToPlaylistPopup
import com.ssw.kast.ui.component.CurrentSongBar
import com.ssw.kast.ui.component.KastLoader
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.SongViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SuggestionsScreen (
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    accountManager: AccountManager,
    songManager: SongManager,
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    // ---------- Fetching data server ----------
    val scope = rememberCoroutineScope()

    var loggedUser by remember { mutableStateOf(User()) }

    LaunchedEffect(Unit) {
        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!

            songViewModel.loadSuggestions(loggedUser.id)
        } else {
            NavigationManager.navigateTo(navController,"sign_in")
        }
    }
    val listSuggestions by songViewModel.suggestions

    val showAddToPlaylistDialog = remember { mutableStateOf(false) }
    val songToAddToPlaylist = remember { mutableStateOf<Song?>(null) }

    var isLoading by remember { mutableStateOf(false) }

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
            BackNavBar(
                title = "Suggestion (${listSuggestions.size})",
                onPressBackButton = {
                    NavigationManager.goToPreviousScreen(
                        navController = navController,
                        selectedItem = selectedItem,
                        onPreviousRouteNull = {
                            NavigationManager.navigateTo(navController,"music")
                        }
                    )
                },
                extraIcon = Icons.Outlined.Refresh,
                onClickExtraIcon = {
                    scope.launch {
                        isLoading = true
                        songViewModel.refreshSuggestions(loggedUser.id)
                        //delay(500)
                        isLoading = false
                    }
                }
            )

            if (isLoading) {
                KastLoader(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    boxSize = 130.dp
                )
            }

            if (listSuggestions.isNotEmpty() && !isLoading) {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                ) {
                    items(listSuggestions) { song ->
                        MusicListCard(
                            song = song,
                            songManager = songManager,
                            onClick = {
                                scope.launch {
                                    try {
                                        songManager.onSongListChange(listSuggestions)
                                        songManager.clickNewSong(song, loggedUser.id, true)
                                        NavigationManager.navigateTo(navController,"player")
                                    } catch (e: Exception) {
                                        Log.e("Suggestions", "Exception: ${e.message}")
                                        e.printStackTrace()
                                    }
                                }
                            },
                            extraIcon = Icons.Outlined.MoreVert,
                            onClickExtraIcon = {
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

@Preview(showBackground = true)
@Composable
fun SuggestionsScreenPreview() {
    KastTheme {
        //val navController = rememberNavController()

        //val selectedItemName by remember { mutableStateOf("home") }
        //val selectedItem = SelectedItemManagement(selectedItemName)

        //val songManager = SongManager()

        //SuggestionsScreen(navController, selectedItem, songManager)
    }
}