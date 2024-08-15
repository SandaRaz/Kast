package com.ssw.kast.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ssw.kast.model.component.MusicCardModel
import com.ssw.kast.model.component.SmallCardModel

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