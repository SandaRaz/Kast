package com.ssw.kast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssw.kast.model.component.ListCardModel
import com.ssw.kast.model.component.MusicCardModel
import com.ssw.kast.model.component.SmallCardModel
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.ui.theme.White

@Composable
fun ListCardVerticalContainer(
    modifier: Modifier = Modifier,
    contentHeight: Dp = 85.dp,
    listListCard: List<ListCardModel> = listOf()
) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .border(
                1.dp,
                Color.Transparent
            )
    ) {
        items(listListCard) { listCard ->
            ListCard(
                title = listCard.title,
                description = listCard.description,
                image = listCard.image,
                containerHeight = contentHeight,
                onClick = listCard.onClick
            )
        }
    }
}

@Composable
fun MusicCardHorizontalContainer(
    modifier: Modifier = Modifier,
    contentWidth: Dp = 145.dp,
    contentHeight: Dp = 150.dp,
    titleColor: Color = White,
    descriptionColor: Color = White.copy(alpha = 0.6f),
    description2Color: Color = LightGrey,
    listMusicCard: List<MusicCardModel> = listOf()
) {
    LazyRow (
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .border(
                1.dp,
                Color.Transparent
            )
    ) {
        items(listMusicCard) { musicCard ->
            MusicCard(
                image = musicCard.image,
                title = musicCard.title,
                titleColor = titleColor,
                description = musicCard.description,
                descriptionColor = descriptionColor,
                description2 = musicCard.description2,
                description2Color = description2Color,
                modifier = Modifier
                    .width(contentWidth)
                    .height(contentHeight),
                onClick = musicCard.onClick
            )
        }
    }
}

@Composable
fun MusicCardVerticalContainer(
    modifier: Modifier = Modifier,
    addMoreSign: Boolean = false,
    listMusicCard: List<MusicCardModel> = listOf()
) {
    val musicCardHeight: Dp = 120.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .heightIn(min = 136.dp, max = 560.dp)
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
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listMusicCard) { musicCard ->
            MusicCard(
                image = musicCard.image,
                title = musicCard.title,
                description = musicCard.description,
                modifier = Modifier
                    .height(musicCardHeight),
                onClick = musicCard.onClick
            )
        }

        if (addMoreSign) {
            items(1) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(musicCardHeight)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 0.dp,
                            color = Darker,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text (
                        text = "...",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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

    LazyRow (
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .border(
                1.dp,
                Color.Transparent
            )
    ) {
        items(listSmallCard) {smallCard ->
            SmallCard(
                image = smallCard.image,
                primaryLabel = smallCard.primaryLabel,
                secondaryLabel = smallCard.secondaryLabel,
                modifier = Modifier
                    .width(smallCardWidth)
                    .height(smallCardHeight),
                onClick = smallCard.onClick
            )
        }
    }
}