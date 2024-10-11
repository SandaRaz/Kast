package com.ssw.kast.ui.screen.music

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.ui.component.BottomNavigationBar
import com.ssw.kast.ui.component.CurrentSongBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.component.SimpleCard
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.SongViewModel

@Composable
fun MusicScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit,
    accountManager: AccountManager,
    songManager: SongManager,
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    // -------------- fetch data from database ----------------

    var loggedUser by remember { mutableStateOf(User()) }

    LaunchedEffect(Unit) {
        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!

            songManager.loadRecentSong()
            songViewModel.loadSuggestions(loggedUser.id)
            playlistViewModel.loadUserPlaylists(loggedUser.id)
        }
    }

    val countRecents = songManager.recentSongs.size
    val countSuggestions = songViewModel.suggestions.value.size
    val countPlaylists = playlistViewModel.playlists.size

    // --------------------------------------------------------

    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {
                bottomNavigationBar()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            val cornerShape = 16.dp

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 512.dp)
                    .border(
                        1.dp,
                        Color.Transparent
                    ),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SimpleCard(
                        label = "Recent",
                        description = "$countRecents songs",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.Outlined.AccessTime,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = {
                            NavigationManager.navigateTo(navController,"recent")
                        }
                    )
                }

                item {
                    SimpleCard(
                        label = "Playlists",
                        description = "$countPlaylists playlists",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.AutoMirrored.Outlined.PlaylistPlay,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = {
                            NavigationManager.navigateTo(navController, "playlist")
                        }
                    )
                }

                item {
                    SimpleCard(
                        label = "Suggestions",
                        description = "$countSuggestions songs",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.AutoMirrored.Outlined.QueueMusic,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = {
                            NavigationManager.navigateTo(navController,"suggestion")
                        }
                    )
                }

                item {
                    SimpleCard(
                        label = "Favorites",
                        description = "0 songs",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.Outlined.StarRate,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(
                        Color.Transparent
                    )
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

@Preview(showBackground = true)
@Composable
fun MusicScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("music") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController, selectedItem)
        }

        val currentSong by remember { mutableStateOf<Song?>(null) }
        val songManager = SongManager(currentSong)

        //MusicScreen(navController, selectedItem, bottomNavigationBar, songManager)
    }
}