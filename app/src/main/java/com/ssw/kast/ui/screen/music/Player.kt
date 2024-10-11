package com.ssw.kast.ui.screen.music

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Forward5
import androidx.compose.material.icons.outlined.Replay5
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.entity.Song
import com.ssw.kast.ui.component.BackNavBar
import com.ssw.kast.ui.component.LegacyBlurImage
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.serializer.DateUtils
import com.ssw.kast.ui.component.CircleLoader
import com.ssw.kast.ui.component.KastLoader
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.viewmodel.SongViewModel
import com.ssw.kast.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import org.threeten.bp.Duration

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    songManager: SongManager,
    accountManager: AccountManager,
    songViewModel: SongViewModel,
    userViewModel: UserViewModel
) {
    // ----------- fetching data ------------

    val defaultSongCover = getCachedImageFromResources(resourceId = R.drawable.default_cover)

    var playSong by remember { mutableStateOf(false) }
    if (songManager.currentSong != null) {
        playSong = songManager.isPlayed
    }

    var currentPosition by remember { mutableLongStateOf(songManager.audioPlayer.player.currentPosition) }
    var totalDuration by remember { mutableLongStateOf(songManager.audioPlayer.player.duration) }

    var timeListeningTheSong by remember { mutableLongStateOf(0L) }

    LaunchedEffect(songManager.currentSong){
        timeListeningTheSong = 0L
    }

    LaunchedEffect(songManager.isPlayed) {
        //Log.d("Player", "LaunchedEffect State: ${songManager.isPlayed}")

        while(songManager.isPlayed) {
            currentPosition = songManager.audioPlayer.player.currentPosition
            totalDuration = songManager.audioPlayer.player.duration

            //Log.d("Player", "CurrentPosition: $currentPosition (${DateUtils.formatTime(currentPosition)})")
            delay(1000L)
        }
    }

    // --------------------------------------

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Box (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .border(
                    1.dp,
                    Color.Transparent
                )
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            val cornerShape = 16.dp
            val backgroundAlphaLevel = 0.6f

            LegacyBlurImage(
                imageBitmap = songManager.currentSong?.getCover(defaultSongCover)!!,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                blur = 16,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BackNavBar(
                    iconColor = MaterialTheme.colorScheme.tertiary,
                    onPressBackButton = {
                        NavigationManager.goToPreviousScreen(
                            navController = navController,
                            selectedItem = selectedItem,
                            onPreviousRouteNull = {
                                selectedItem.onSelectItem("music")
                                NavigationManager.navigateTo(navController,"music")
                            }
                        )
                    },
                    modifier = Modifier
                        .background(
                            color = Color.Black.copy(alpha = backgroundAlphaLevel)
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                        .background(
                            color = Color.Black.copy(alpha = backgroundAlphaLevel)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = songManager.currentSong?.getCover(defaultSongCover)!!,
                        contentDescription = "Song cover",
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .aspectRatio(1f)
                            .shadow(
                                elevation = 16.dp,
                                ambientColor = Color.Black,
                                spotColor = Color.Black,
                                shape = RoundedCornerShape(cornerShape)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(cornerShape)
                            )
                            .clip(RoundedCornerShape(cornerShape)),
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 1.dp,
                                color = Color.Transparent
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.1f),
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Black.copy(alpha = 0.8f),
                                        Color.Black.copy(alpha = 0.9f)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    Color.Transparent
                                )
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            if (songManager.songLoading.value) {
                                Box (
                                    modifier = Modifier
                                        .size(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircleLoader(
                                        isVisible = true,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else {
                                Text(
                                    text = songManager.currentSong?.title!!,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .basicMarquee(
                                            delayMillis = 7000,
                                            iterations = Int.MAX_VALUE
                                        )
                                )

                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(16.dp)
                                        .background(
                                            Color.Transparent
                                        )
                                )

                                Text(
                                    text = songManager.currentSong?.singer!!,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .basicMarquee(
                                            delayMillis = 7000,
                                            iterations = Int.MAX_VALUE
                                        )
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

                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    Color.Transparent
                                )
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            LinearProgressIndicator(
                                progress = {
                                    if (totalDuration > 0)
                                        currentPosition / totalDuration.toFloat()
                                    else
                                        0f
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = DateUtils.formatTime(currentPosition),
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = DateUtils.formatTime(totalDuration),
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    Color.Transparent
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val bigIconSize = 64.dp
                            val mediumIconSize = 36.dp
                            val smallIconSize = 28.dp
                            val playIcon: ImageVector = if (playSong) Icons.Filled.PauseCircle
                                else Icons.Filled.PlayCircle

                            Icon(
                                imageVector = Icons.Outlined.SkipPrevious,
                                contentDescription = "Previous song",
                                modifier = Modifier
                                    .size(smallIconSize)
                                    .clickable {
                                        songManager.previousSong(songManager.songList.value)
                                    },
                                tint = MaterialTheme.colorScheme.tertiary
                            )

                            Icon(
                                imageVector = Icons.Outlined.Replay5,
                                contentDescription = "Replay 5 seconds",
                                modifier = Modifier
                                    .size(mediumIconSize)
                                    .clickable {
                                        songManager.seekBackward()
                                    },
                                tint = MaterialTheme.colorScheme.tertiary
                            )

                            Icon(
                                imageVector = playIcon,
                                contentDescription = "Play song",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(bigIconSize)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 12.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        playSong = !playSong
                                        if (songManager.currentSong != null) {
                                            if (songManager.isPlayed) {
                                                songManager.pauseSong()
                                            } else {
                                                songManager.playSong(false)
                                            }
                                            songManager.isPlayed = playSong
                                        }
                                    }
                            )

                            Icon(
                                imageVector = Icons.Outlined.Forward5,
                                contentDescription = "Forward 5 seconds",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier
                                    .size(mediumIconSize)
                                    .clickable {
                                        songManager.seekForward()
                                    }
                            )

                            Icon(
                                imageVector = Icons.Outlined.SkipNext,
                                contentDescription = "Next song",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier
                                    .size(smallIconSize)
                                    .clickable {
                                        songManager.nextSong(songManager.songList.value)
                                    }
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(
                                    Color.Transparent
                                )
                        )

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("home") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val currentSong by remember { mutableStateOf<Song?>(null) }
        val songManager = SongManager(currentSong)

        //PlayerScreen(navController, selectedItem, songManager)
    }
}