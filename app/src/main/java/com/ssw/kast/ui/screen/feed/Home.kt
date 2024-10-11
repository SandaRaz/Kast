package com.ssw.kast.ui.screen.feed

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.ssw.kast.R
import com.ssw.kast.ui.component.LogoHeader
import com.ssw.kast.ui.component.MusicCardHorizontalContainer
import com.ssw.kast.ui.component.MusicCardVerticalContainer
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.ui.component.SmallCardContainer
import com.ssw.kast.ui.component.TitleBar
import com.ssw.kast.model.component.MusicCardModel
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.component.StartupPopUp
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.UserViewModel
import com.ssw.kast.viewmodel.SongViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    accountManager: AccountManager,
    songManager: SongManager,
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit,
    userViewModel: UserViewModel,
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    // ------- fetching data -------

    val scope = rememberCoroutineScope()

    var loggedUser by remember { mutableStateOf(User()) }

    val defaultProfilePicture = getCachedImageFromResources(resourceId = R.drawable.default_profil)
    val defaultSongCover = getCachedImageFromResources(resourceId = R.drawable.default_cover)
    val defaultCategoryCover = getCachedImageFromResources(resourceId = R.drawable.default_category_cover)
    val defaultPlaylistCover = getCachedImageFromResources(resourceId = R.drawable.default_playlist_cover)

    var recentPlayedDesc by remember { mutableStateOf<List<Song>>(emptyList()) }
    val recentPlayedMusicCards = remember { mutableStateListOf<MusicCardModel>() }

    LaunchedEffect(Unit) {
        // ----- Loading current logged user -----

        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!

            // ----- Load newest user (recently join)
            userViewModel.loadNewestUsers(
                accountManager,
                navController,
                loggedUser.id,
                5,
                defaultProfilePicture
            )

            // --- Load most streamed songs ---
            songViewModel.loadMostStreamedSongs(
                navController,
                songManager,
                loggedUser.id,
                5,
                defaultSongCover
            )

            // ----- Load top categories ------
            songViewModel.loadTopCategories(
                navController,
                songManager,
                defaultCategoryCover
            )

            songManager.loadRecentSong(loggedUser.id)
            recentPlayedDesc = songManager.recentSongs.reversed().take(5)
            // if (!content didnt change) => { }
            recentPlayedDesc.forEach { song ->
                recentPlayedMusicCards.add(
                    MusicCardModel (
                        title = song.title,
                        description = song.singer,
                        image = song.cover ?: defaultSongCover,
                        onClick = {
                            scope.launch {
                                try {
                                    songManager.onSongListChange(songManager.recentSongs)
                                    songManager.clickNewSong(song, loggedUser.id,true)
                                    NavigationManager.navigateTo(navController,"player")
                                } catch (e: Exception) {
                                    Log.e("Home Recent Played", "Exception: ${e.message}")
                                    e.printStackTrace()
                                }
                            }
                        }
                    )
                )
            }

            playlistViewModel.loadPlaylistCards(navController, loggedUser.id, 5, defaultPlaylistCover)

            // ------- save listened songs -------
            userViewModel.saveListenedSongs(loggedUser, songManager)
        } else {
            NavigationManager.navigateTo(navController,"sign_in")
        }
        // --- End loading current logged user ---
    }
    val mostStreamedCards by songViewModel.mostStreamedCards
    val newestUserSmallCards by userViewModel.newestUserSmallCards
    val top5Categories by songViewModel.topCategoriesCards
    val playlist5Cards by playlistViewModel.playlistCard

    LaunchedEffect(accountManager.isOnStartup()) {
        delay(7000)
        accountManager.switchOffStartupState()
    }

    // ----- End fetching data -----

    if (accountManager.isOnStartup()) {
        StartupPopUp(isShowed = accountManager.startupState())
    } else {
        Scaffold (
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background),
            bottomBar = {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    bottomNavigationBar()
                }
            }
        ) { innerPadding ->
            Box (
                modifier = Modifier
                    .border(
                        1.dp,
                        Color.Transparent
                    )
            ) {
                Row (
                    modifier = Modifier
                        .statusBarsPadding()
                        .zIndex(20f)
                        .fillMaxWidth()
                        .border(
                            2.dp,
                            Color.Transparent
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        bitmap = loggedUser.profilePicture ?: defaultProfilePicture,
                        contentDescription = "User account",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(38.dp)
                            .aspectRatio(1f)
                            .border(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable {
                                accountManager.viewedUser = loggedUser
                                NavigationManager.navigateTo(navController,"profile")
                            }
                    )
                }

                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    LogoHeader()

                    HorizontalDivider(thickness = 2.dp,color = MaterialTheme.colorScheme.primary)

                    Spacer(modifier = Modifier.height(16.dp))

                    if (mostStreamedCards.isNotEmpty()) {
                        TitleBar(
                            title = "Most listened",
                            extraIcon = Icons.Outlined.Refresh,
                            onClickExtraIcon = {
                                scope.launch {
                                    songViewModel.refreshMostStreamedSongs(
                                        navController = navController,
                                        songManager = songManager,
                                        userId = loggedUser.id,
                                        amount = 5,
                                        defaultCover = defaultSongCover
                                    )
                                }
                            },
                            isNavigable = true,
                            onNavigationClick = {
                                NavigationManager.navigateTo(navController,"streamed")
                            }
                        )
                        MusicCardHorizontalContainer(
                            listMusicCard = mostStreamedCards,
                            contentWidth = 140.dp,
                            contentHeight = 140.dp,
                            description2Color = MaterialTheme.colorScheme.primary
                        )
                    }


                    TitleBar(
                        title = "Recently join",
                        extraIcon = Icons.Outlined.Refresh,
                        onClickExtraIcon = {
                            scope.launch {
                                userViewModel.refreshNewestUsers(
                                    accountManager = accountManager,
                                    navController = navController,
                                    loggedUserId = loggedUser.id,
                                    amount = 5,
                                    defaultProfilePicture = defaultProfilePicture
                                )
                            }
                        }
                    )
                    SmallCardContainer(
                        listSmallCard = newestUserSmallCards
                    )

                    if (top5Categories.isNotEmpty()) {
                        TitleBar(
                            title = "Top categories",
                            extraIcon = Icons.Outlined.Refresh,
                            onClickExtraIcon = {
                                scope.launch {
                                    songViewModel.refreshTopCategories(
                                        navController = navController,
                                        songManager = songManager,
                                        categoriesCover = defaultCategoryCover
                                    )
                                }
                            },
                            isNavigable = true,
                            onNavigationClick = {
                                NavigationManager.navigateTo(navController,"categories")
                            }
                        )
                        MusicCardHorizontalContainer(
                            listMusicCard = top5Categories,
                            contentWidth = 140.dp,
                            contentHeight = 140.dp,
                            descriptionColor = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (recentPlayedMusicCards.size > 0) {
                        TitleBar(
                            title = "Recent played",
                            isNavigable = true,
                            onNavigationClick = {
                                NavigationManager.navigateTo(navController, "recent")
                            },
                            extraIcon = Icons.Outlined.Refresh,
                            onClickExtraIcon = {
                                scope.launch {
                                    songManager.refreshRecentSong(loggedUser.id)
                                }
                            }
                        )
                        MusicCardHorizontalContainer(
                            listMusicCard = recentPlayedMusicCards,
                            contentWidth = 140.dp,
                            contentHeight = 140.dp
                        )
                    }

                    if (playlist5Cards.isNotEmpty()) {
                        TitleBar(
                            title = "Playlist",
                            extraIcon = Icons.Outlined.Refresh,
                            onClickExtraIcon = {
                                scope.launch {
                                    playlistViewModel.refreshPlaylistCards(navController, 5, defaultPlaylistCover)
                                }
                            },
                            isNavigable = true,
                            onNavigationClick = {
                                selectedItem.onSelectItem("music")
                                NavigationManager.navigateTo(navController,"playlist")
                            }
                        )
                        MusicCardVerticalContainer(
                            addMoreSign = (playlistViewModel.playlists.size > 5),
                            listMusicCard = playlist5Cards
                        )
                    }

                    /* ------- new release -------- */

                    /* ------- Popular today ------ */

                    /* ------- From followed ------ */

                }
            }
        }
    }
}