package com.ssw.kast.ui.component

import android.os.Build
import android.os.Build.VERSION
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.cloudy.Cloudy
import com.skydoves.cloudy.internals.render.RenderScriptToolkit
import com.ssw.kast.ui.theme.montserrat

@Composable
fun BlurredImage(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    contentDescription: String?,
    radius: Int,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    if (VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Image(
            painter = BitmapPainter(image),
            contentDescription = contentDescription,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            modifier = modifier
                .blur(radius = radius.dp)
        )
    } else {
        Cloudy (radius = radius) {
            Image(
                painter = BitmapPainter(image),
                contentDescription = contentDescription,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                modifier = modifier
            )
        }
    }
}

@Composable
fun FieldGroupTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
    )

    Row (
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Color.Transparent
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(2.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(
                    1.dp,
                    Color.Transparent
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(2.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground
                )
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
    )
}

@Composable
fun LegacyBlurImage(
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Fit,
    blur: Int
) {
    if (VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Image(
            bitmap = imageBitmap,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier.blur(blur.dp)
        )
    } else {
        val bitmap = imageBitmap.asAndroidBitmap().let { RenderScriptToolkit.blur(it, blur) }
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LogoHeader(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    letterSpacing: TextUnit = 16.sp
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    Color.Transparent
                )
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "KAST",
                    fontFamily = montserrat,
                    style = textStyle,
                    letterSpacing = letterSpacing,
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