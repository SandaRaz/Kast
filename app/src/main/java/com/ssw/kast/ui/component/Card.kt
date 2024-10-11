package com.ssw.kast.ui.component

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.ssw.kast.R
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.Grey
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    image: ImageBitmap? = null,
    containerHeight: Dp = 85.dp,
    upperLine: Boolean = true,
    underLine: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(containerHeight)
    ) {
        if (upperLine) {
            Spacer (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                    )
            )
        }

        Row(
            modifier = modifier
                .fillMaxSize()
                .border(
                    1.dp,
                    Color.Transparent
                )
                .clickable {
                    onClick()
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val coverCornerShape = 8.dp

            image?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(coverCornerShape))
                )
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(
                            delayMillis = 7000,
                            iterations = Int.MAX_VALUE
                        )
                )

                description?.let {
                    Spacer (
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .border(
                                1.dp,
                                Color.Transparent
                            )
                    )

                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
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
        }

        if (underLine) {
            Spacer (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    title: String,
    titleColor: Color = White,
    description: String? = null,
    descriptionColor: Color = White.copy(alpha = 0.6f),
    description2: String? = null,
    description2Color: Color = LightGrey,
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
            .padding(0.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = BitmapPainter(image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRound)),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(30, 30, 30, 180),
                            Color(30, 30, 30, 150),
                            Color(30, 30, 30, 150),
                            Color(30, 30, 30, 130),
                            Color(30, 30, 30, 120),
                            Color(30, 30, 30, 110),
                            Color(30, 30, 30, 90),
                            Color(30, 30, 30, 70),
                            Color(30, 30, 30, 50),
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
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(
                            delayMillis = 10000,
                            iterations = Int.MAX_VALUE
                        )
                )
            }

            description?.let {
                Spacer(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .height(4.dp)
                )
                Row {
                    Text (
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = descriptionColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .basicMarquee(
                                delayMillis = 10000,
                                iterations = Int.MAX_VALUE
                            )
                    )
                }
            }

            description2?.let {
                Spacer(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .height(4.dp)
                )
                Row {
                    Text (
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = description2Color,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .basicMarquee(
                                delayMillis = 10000,
                                iterations = Int.MAX_VALUE
                            )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicListCard(
    modifier: Modifier = Modifier,
    songManager: SongManager,
    song: Song,
    showListeners: Boolean = false,
    onClick: () -> Unit = {},
    isFavorite: Boolean = false,
    addToFavorite: () -> Unit = {},
    extraIcon: ImageVector? = null,
    onClickExtraIcon: () -> Unit = {}
) {
    // ------- data -------

    var isPlayed by remember { mutableStateOf(false) }
    if (songManager.isCurrent(song)) {
        isPlayed = songManager.isPlayed
    }

    val playIcon: ImageVector = if (isPlayed) Icons.Filled.PauseCircle
    else Icons.Filled.PlayCircle

    song.loadCoverBitmap()
    val coverImage: ImageBitmap = song.cover ?: getCachedImageFromResources(resourceId = R.drawable.default_cover)

    // --------------------


    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer (
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                )
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(85.dp)
                .border(
                    1.dp,
                    Color.Transparent
                )
                .clickable {
                    onClick()
                }
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 4.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val coverCornerShape = 8.dp

                Box (
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(coverCornerShape)
                        )
                ) {
                    Image(
                        bitmap = coverImage,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(coverCornerShape)),
                        contentScale = ContentScale.FillBounds
                    )

                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                2.dp,
                                Color.Transparent
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon (
                            imageVector = playIcon,
                            contentDescription = null,
                            tint = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier
                                .zIndex(10f)
                                .background(
                                    color = Color.White.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .size(30.dp)
                        )
                    }
                }

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
                        text = song.title,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .basicMarquee(
                                delayMillis = 7000,
                                iterations = Int.MAX_VALUE
                            )
                    )

                    Text(
                        text = song.singer,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .basicMarquee(
                                delayMillis = 7000,
                                iterations = Int.MAX_VALUE
                            )
                    )

                    if (showListeners) {
                        Text(
                            text = "${song.listeners} listener" + if (song.listeners > 1) "s" else "",
                            color = LightGrey,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .basicMarquee(
                                    delayMillis = 8000,
                                    iterations = Int.MAX_VALUE
                                )
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = Color.Transparent)
                )
            }

            val favoriteIcon = if (isFavorite) Icons.Filled.StarRate
            else Icons.Outlined.StarRate
            val favoriteIconTint = if (isFavorite) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.tertiary

            /*

            Spacer (
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(
                        color = Color.Transparent
                    )
            )

            Icon (
                imageVector = favoriteIcon,
                contentDescription = null,
                tint = favoriteIconTint,
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .clickable {
                        addToFavorite()
                    }
            )
            */

            extraIcon?.let {
                Spacer (
                    modifier = Modifier
                        .width(8.dp)
                        .fillMaxHeight()
                        .background(
                            color = Color.Transparent
                        )
                )

                Icon (
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(32.dp)
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                        .clickable {
                            onClickExtraIcon()
                        }
                )
            }
        }

        Spacer (
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleCard(
    modifier: Modifier = Modifier,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.tertiary,
    description: String?,
    descriptionColor: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.tertiary,
    shape: Shape = RectangleShape,
    onClick: () -> Unit = {}
) {
    Row (
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            )
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Card icon",
                tint = iconTint,
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        1.dp,
                        Color.Transparent
                    )
            )

            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(color = Color.Transparent)
            )
        }

        Column (
            modifier = Modifier
                .border(
                    1.dp,
                    Color.Transparent
                )
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                color = labelColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .basicMarquee(
                        delayMillis = 5000,
                        iterations = Int.MAX_VALUE
                    )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp)
                    .background(color = Color.Transparent)
            )

            if (description != null) {
                Text(
                    text = description,
                    color = descriptionColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(
                            delayMillis = 5000,
                            iterations = Int.MAX_VALUE
                        )
                )
            }
        }
    }
}

@Composable
fun SmallCard(
    image: ImageBitmap,
    primaryLabel: String,
    secondaryLabel: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
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
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = BitmapPainter(image),
            contentScale = ContentScale.FillWidth,
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
                )
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