package com.ssw.kast.ui.screen.search

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.ui.component.BottomNavigationBar
import com.ssw.kast.ui.component.SearchBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.model.component.SearchResultModel
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.component.AddToPlaylistPopup
import com.ssw.kast.ui.component.CircleLoader
import com.ssw.kast.ui.component.CurrentSongBar
import com.ssw.kast.ui.component.KastLoader
import com.ssw.kast.ui.component.ListCard
import com.ssw.kast.ui.component.LogoHeader
import com.ssw.kast.ui.component.MusicListCard
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.Grey
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit,
    accountManager: AccountManager,
    songManager: SongManager,
    searchViewModel: SearchViewModel,
    playlistViewModel: PlaylistViewModel
) {
    // ---------- Fetching data server ----------
    val scope = rememberCoroutineScope()

    var loggedUser by remember { mutableStateOf(User()) }

    LaunchedEffect(Unit) {
        val isLoggedUser = accountManager.loadLoggedUser()
        if (isLoggedUser) {
            loggedUser = accountManager.currentUser!!
        } else {
            NavigationManager.navigateTo(navController,"sign_in")
        }
    }
    var searchedValue by remember { mutableStateOf(TextFieldValue("")) }
    val searchResult by searchViewModel.searchResult

    val showAddToPlaylistDialog = remember { mutableStateOf(false) }
    val songToAddToPlaylist = remember { mutableStateOf<Song?>(null) }

    val defaultProfilePicture = getCachedImageFromResources(resourceId = R.drawable.default_profil)

    val cornerShape = RoundedCornerShape(12.dp)

    val keyboardController = LocalSoftwareKeyboardController.current

    var isLoading by remember { mutableStateOf(false) }

    // ------------------------------------------

    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        bottomBar = {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {
                bottomNavigationBar()
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            SearchBar(
                searchValue = searchedValue,
                onValueChange = { newValue ->
                    searchedValue = newValue
                },
                onPrevious = {
                    searchViewModel.searchResult.value.empty()
                    NavigationManager.goToPreviousScreen(
                        navController = navController,
                        selectedItem = selectedItem,
                        onPreviousRouteNull = {
                            NavigationManager.navigateTo(navController, "home")
                        }
                    )
                },
                onSearch = {
                    searchViewModel.searchResult.value.empty()
                    keyboardController?.hide()
                    scope.launch {
                        if (searchedValue.text.isNotBlank()) {
                            isLoading = true
                            searchViewModel.search(loggedUser.id, searchedValue.text)
                            isLoading = false
                        }
                    }
                }
            )

            HorizontalDivider(thickness = 2.dp,color = MaterialTheme.colorScheme.primary)

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    KastLoader(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        boxSize = 130.dp
                    )
                }

                if (searchResult.songs.isNotEmpty()) {
                    Text(
                        text = "Song results",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.surface
                            )
                    ) {
                        items(searchResult.songs) { song ->
                            MusicListCard(
                                songManager = songManager,
                                song = song,
                                showListeners = true,
                                onClick = {
                                    scope.launch {
                                        try {
                                            songManager.onSongListChange(listOf(song))
                                            songManager.clickNewSong(song, loggedUser.id, true)
                                            NavigationManager.navigateTo(navController,"player")
                                        } catch (e: Exception) {
                                            Log.e("Search", "Exception: ${e.message}")
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
                }

                AddToPlaylistPopup(
                    isShowed = showAddToPlaylistDialog,
                    song = songToAddToPlaylist,
                    userId = loggedUser.id,
                    playlistViewModel = playlistViewModel
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (searchResult.users.isNotEmpty()) {
                    Text(
                        text = "User results",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .border(
                                width = 3.dp,
                                color = MaterialTheme.colorScheme.surface
                            )
                    ) {
                        items (searchResult.users) { user ->
                            user.loadBitmapProfilePicture()

                            ListCard(
                                title = user.username,
                                description = "${user.followersCount} followers",
                                image = user.profilePicture ?: defaultProfilePicture,
                                onClick = {
                                    try {
                                        accountManager.viewedUser = user
                                        NavigationManager.navigateTo(navController,"profile")
                                    } catch (e: Exception) {
                                        Log.e("Search", "Exception: ${e.message}")
                                        e.printStackTrace()
                                    }
                                }
                            )
                        }
                    }
                }
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

@Composable
fun SearchResult(
    model: SearchResultModel,
    resultTypeIcon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cornerShape = 12.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(cornerShape)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerShape)
            )
            .clickable {
                onClick()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val imageCornerShape = 8.dp
        Image(
            bitmap = model.image,
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(imageCornerShape))
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(imageCornerShape)
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                ),
                verticalArrangement = Arrangement.Center
        ) {
            if(model.primaryLabel.isNotBlank()){
                Text(
                    text = model.primaryLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if(model.secondLabel.isNotBlank()){
                Text(
                    text = model.secondLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Icon(
            imageVector = resultTypeIcon,
            contentDescription = null,
            tint = LightGrey,
            modifier = Modifier
                .size(25.dp)
        )
    }
}

@Composable
fun SearchResultContainer(
    title: String,
    searchResults: List<SearchResultModel> = listOf(),
    resultTypeIcon: ImageVector,
    navController: NavHostController
) {
    val cornerShape = 16.dp
    val resultHeight = 80.dp

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.25f),
                        Color.Black.copy(alpha = 0.25f),
                        Color.Black.copy(alpha = 0.25f),
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.6f)
                    )
                ),
                shape = RoundedCornerShape(cornerShape)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
        )

        if (searchResults.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(resultHeight)
                    .border(1.dp, Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "- No result -",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Grey
                )
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp, max = 240.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(cornerShape)
                    ),
            ) {
                items(searchResults) { result ->
                    SearchResult(
                        model = result,
                        resultTypeIcon = resultTypeIcon,
                        modifier = Modifier
                            .height(resultHeight)
                            .padding(vertical = 6.dp),
                        onClick = {
//                    val navigationRoute = "${result.route}/{${result.id}}"
//                    navController.navigate(navigationRoute)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("search") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController, selectedItem)
        }

        //SearchScreen(navController, selectedItem, bottomNavigationBar)
    }
}