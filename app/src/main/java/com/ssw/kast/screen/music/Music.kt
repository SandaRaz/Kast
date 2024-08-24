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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.component.BottomNavigationBar
import com.ssw.kast.component.SelectedItemManagement
import com.ssw.kast.component.SimpleCard
import com.ssw.kast.model.entity.getImageFromResources
import com.ssw.kast.screen.menu.MenuScreen
import com.ssw.kast.ui.theme.KastTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit
) {
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
                        description = "0 songs",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.Outlined.AccessTime,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    SimpleCard(
                        label = "Playlists",
                        description = "0 playlists",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.AutoMirrored.Outlined.PlaylistPlay,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    SimpleCard(
                        label = "Suggestions",
                        description = "10 songs",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.AutoMirrored.Outlined.QueueMusic,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    SimpleCard(
                        label = "Local Songs",
                        description = "303 songs",
                        shape = RoundedCornerShape(cornerShape),
                        icon = Icons.Outlined.Folder,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = getImageFromResources(resourceId = R.drawable.roch_voisine),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(
                        modifier = Modifier
                            .width(16.dp)
                            .fillMaxHeight()
                            .background(color = Color.Transparent)
                    )

                    Column (
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Ne viens pas",
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .basicMarquee(iterations = Int.MAX_VALUE)
                        )

                        Text(
                            text = "Roch Voisine",
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    }
                }

                Icon (
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(34.dp)
                        .clickable {
                            navController.navigate("player")
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

        MenuScreen(navController, selectedItem, bottomNavigationBar)
    }
}