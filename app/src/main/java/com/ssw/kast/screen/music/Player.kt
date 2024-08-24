package com.ssw.kast.screen.music

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeDown
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ssw.kast.component.BackNavBar
import com.ssw.kast.component.LegacyBlurImage
import com.ssw.kast.component.SelectedItemManagement
import com.ssw.kast.model.entity.getImageFromResources
import com.ssw.kast.ui.theme.KastTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement
) {
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
            // ----------- fetching data ------------
            val testImage = getImageFromResources(resourceId = R.drawable.roch_voisine)


            var playSong by remember { mutableStateOf(false) }
            // --------------------------------------

            val cornerShape = 16.dp
            val backgroundAlphaLevel: Float = 0.5f

            LegacyBlurImage(
                imageBitmap = testImage,
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
                    onPressBackButton = {
                        val previousBackStackEntry = navController.previousBackStackEntry
                        val previousRoute = previousBackStackEntry?.destination?.route
                        if (previousRoute != null) {
                            selectedItem.onSelectItem(previousRoute)
                            navController.popBackStack()
                        } else {
                            selectedItem.onSelectItem("music")
                            navController.navigate("music")
                        }
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
                        )
                ) {
                    Image(
                        bitmap = testImage,
                        contentDescription = "Song cover",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .shadow(elevation = 16.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(
                                Color.Transparent
                            )
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
                            Text(
                                text = "Espesso",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .basicMarquee(iterations = Int.MAX_VALUE)
                            )

                            Text(
                                text = "Sabrina Carpenter",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .basicMarquee(iterations = Int.MAX_VALUE)
                            )
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
                                progress = { 0.6f },
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
                                    text = "1:30",
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = "3:45",
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
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val bigIconSize = 72.dp
                            val mediumIconSize = 44.dp
                            val smallIconSize = 36.dp
                            val playIcon: ImageVector = if (playSong) Icons.Filled.PauseCircle
                            else Icons.Filled.PlayCircle

                            Icon(
                                imageVector = Icons.Outlined.SkipPrevious,
                                contentDescription = "Previous song",
                                modifier = Modifier
                                    .size(smallIconSize),
                                tint = MaterialTheme.colorScheme.tertiary
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.VolumeDown,
                                contentDescription = "Previous song",
                                modifier = Modifier
                                    .size(mediumIconSize),
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
                                    }
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                contentDescription = "Previous song",
                                modifier = Modifier
                                    .size(mediumIconSize),
                                tint = MaterialTheme.colorScheme.tertiary
                            )

                            Icon(
                                imageVector = Icons.Outlined.SkipNext,
                                contentDescription = "Next song",
                                modifier = Modifier
                                    .size(smallIconSize),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
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

        PlayerScreen(navController, selectedItem)
    }
}