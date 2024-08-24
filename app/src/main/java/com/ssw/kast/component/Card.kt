package com.ssw.kast.component

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.Grey
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.ui.theme.White

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
            .padding(0.dp)
            .clickable { onClick() }
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
                    .basicMarquee(iterations = Int.MAX_VALUE)
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
                        .basicMarquee(iterations = Int.MAX_VALUE)
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
            .padding(horizontal = 16.dp)
            .clickable {
                onClick()
            },
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