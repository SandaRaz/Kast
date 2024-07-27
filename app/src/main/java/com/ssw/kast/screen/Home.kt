package com.ssw.kast.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Radio
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.model.component.MusicCardModel
import com.ssw.kast.model.component.SmallCardModel
import com.ssw.kast.model.getImageFromResources
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.Grey
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.ui.theme.White

@Composable
fun HomeScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit
) {
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
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState(), true, null, false)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            LogoHeader()

            TitleBar(
                title = "Musics",
                isNavigable = true,
                onClick = {
                    selectedItem.onSelectItem("content_details")
                    navController.navigate("content_details")
                }
            )

            val testImage: ImageBitmap = getImageFromResources(resourceId = R.drawable.ic_launcher_foreground)
            val musicCards: List<MusicCardModel> = listOf(
                MusicCardModel("Everybody knows","John Legend", testImage),
                MusicCardModel("Titre1","Singer1", getImageFromResources(resourceId = R.drawable.kast_old)),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre2","Singer2", testImage),
                MusicCardModel("Titre3","Singer3", testImage)
            )
            MusicCardVerticalContainer(listMusicCard = musicCards)

            TitleBar(title = "Users", isNavigable = true)

            val testImage2: ImageBitmap = getImageFromResources(resourceId = R.drawable.ic_launcher_background)
            val smallCards: List<SmallCardModel> = listOf(
                SmallCardModel(testImage2, "anonymous7_anonymous7", "5 followers"),
                SmallCardModel(testImage2, "xoxo10", "17 followers"),
                SmallCardModel(testImage2, "peanuts_", "8 followers"),
            )
            SmallCardContainer(listSmallCard = smallCards)

            TitleBar(title = "Musics")
            MusicCardHorizontalContainer(listMusicCard = musicCards)

            TitleBar(title = "Musics")
            MusicCardHorizontalContainer(listMusicCard = musicCards)
        }
    }
}

@Composable
fun LogoHeader() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    ambientColor = MaterialTheme.colorScheme.secondary,
                    spotColor = MaterialTheme.colorScheme.secondary
                )
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "KAST",
                    style = MaterialTheme.typography.headlineLarge,
                    letterSpacing = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Filled.LibraryMusic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    }
}

@SuppressLint("PrivateResource")
@Composable
fun TitleBar(title: String, isNavigable: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        if(isNavigable) {
            Image(
                painter = painterResource(id = com.google.android.material.R.drawable.material_ic_keyboard_arrow_next_black_24dp),
                contentDescription = "Advanced icon",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onClick() }
            )
        }
    }
}

@Composable
fun Margin(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.padding(start = start, top = top, end = end, bottom = bottom)) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicCard(
    image: ImageBitmap,
    title: String,
    singer: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cornerRound: Dp = 12.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerRound)
            )
            .border(
                width = 0.dp,
                color = Darker,
                shape = RoundedCornerShape(cornerRound)
            )
            .clickable { onClick() }
            .padding(0.dp)
    ) {
        Image(
            painter = BitmapPainter(image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRound)),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(30, 30, 30, 100),
                            Color(30, 30, 30, 50),
                            Color(30, 30, 30, 25),
                            Color(30, 30, 30, 10),
                        )
                    ),
                    shape = RoundedCornerShape(0.dp, 0.dp, cornerRound, cornerRound)
                )
                .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 10.dp)
        ){
            Row {
                Text (
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(iterations = Int.MAX_VALUE)
                )
            }
            Spacer(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .height(4.dp)
            )
            Row {
                Text (
                    text = singer,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = LightGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(iterations = Int.MAX_VALUE)
                )
            }
        }
    }
}

@Composable
fun MusicCardHorizontalContainer(
    modifier: Modifier = Modifier,
    listMusicCard: List<MusicCardModel> = listOf()
) {
    val musicCardWidth: Dp = 130.dp
    val musicCardHeight: Dp = 150.dp

    Margin (start = 0.dp, end = 0.dp) {
        Row (
            modifier = modifier
                .border(1.dp, Color.Transparent)
                .horizontalScroll(ScrollState(0), true, null, false)
        ) {
            for(musicCard in listMusicCard) {
                var endMargin = 0.dp
                if(listMusicCard.indexOf(musicCard) == listMusicCard.lastIndex) endMargin = 16.dp

                Margin(start = 16.dp, top = 16.dp, end = endMargin, bottom = 16.dp){
                    MusicCard(
                        image = musicCard.image,
                        title = musicCard.title,
                        singer = musicCard.singer,
                        modifier = Modifier
                            .width(musicCardWidth)
                            .height(musicCardHeight)
                    )
                }
            }
        }
    }
}

@Composable
fun MusicCardVerticalContainer(
    modifier: Modifier = Modifier,
    listMusicCard: List<MusicCardModel> = listOf()
) {
    val musicCardHeight: Dp = 120.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .heightIn(min = 152.dp, max = 560.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.1f),
                        Color.Black.copy(alpha = 0.2f),
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.6f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listMusicCard) { musicCard ->
            MusicCard(
                image = musicCard.image,
                title = musicCard.title,
                singer = musicCard.singer,
                modifier = Modifier
                    .height(musicCardHeight)
            )
        }
    }
}


@Composable
fun SmallCard(
    image: ImageBitmap,
    primaryLabel: String,
    secondaryLabel: String,
    modifier: Modifier = Modifier
) {
    val cornerRound: Dp = 12.dp

    Row (
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerRound)
            )
            .border(
                width = 0.dp,
                color = Darker,
                shape = RoundedCornerShape(cornerRound)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = BitmapPainter(image),
            contentDescription = null,
            modifier = Modifier
//                .size(48.dp)
                .clip(CircleShape)
                .fillMaxHeight(0.6f)
                .aspectRatio(1f)
                .border(
                    width = 2.dp,
                    color = Grey,
                    shape = CircleShape
                ),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier
            .width(16.dp)
            .fillMaxHeight()
            .background(Color.Transparent)
        )
        Column {
            Text (
                text = primaryLabel,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text (
                text = secondaryLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = LightGrey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SmallCardContainer(
    modifier: Modifier = Modifier,
    listSmallCard: List<SmallCardModel> = listOf()
) {
    val smallCardWidth: Dp = 200.dp
    val smallCardHeight: Dp = 80.dp

    Margin (start = 0.dp, end = 0.dp) {
        Row (
            modifier = modifier
                .border(1.dp, Color.Transparent)
                .horizontalScroll(ScrollState(0), true, null, false)
        ) {
            for(smallCard in listSmallCard) {
                var endMargin = 0.dp
                if(listSmallCard.indexOf(smallCard) == listSmallCard.lastIndex) endMargin = 16.dp

                Margin(start = 16.dp, top = 16.dp, end = endMargin, bottom = 16.dp){
                    SmallCard(
                        image = smallCard.image,
                        primaryLabel = smallCard.primaryLabel,
                        secondaryLabel = smallCard.secondaryLabel,
                        modifier = Modifier
                            .width(smallCardWidth)
                            .height(smallCardHeight)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("home") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController = navController, selectedItem = selectedItem)
        }

        HomeScreen(navController, selectedItem, bottomNavigationBar)
    }
}