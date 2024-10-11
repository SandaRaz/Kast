package com.ssw.kast.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class StrokeStyle(
    val width: Dp = 4.dp,
    val strokeCap: StrokeCap = StrokeCap.Round,
    val glowRadius: Dp? = 4.dp
)

@Composable
fun CircleLoader(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    color: Color,
    secondColor: Color? = color,
    tailLength: Float = 140f,
    smoothTransition: Boolean = true,
    strokeStyle: StrokeStyle = StrokeStyle(),
    cycleDuration: Int = 1400
) {
    val transition = rememberInfiniteTransition(label = "Circle Loader InfiniteTransition")
    val spinAngel by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "Circle Loader animate float SpinAngel"
    )

    val tailToDisplay = remember { Animatable(0f) }

    LaunchedEffect(isVisible) {
        val targetTail = if (isVisible) tailLength else 0f
        when {
            smoothTransition -> tailToDisplay.animateTo(
                targetValue = targetTail,
                animationSpec = tween(cycleDuration, easing = LinearEasing)
            )
            else -> tailToDisplay.snapTo(targetTail)
        }
    }

    Canvas (
        modifier = modifier
            .rotate(spinAngel)
            .aspectRatio(1f)
    ) {
        listOfNotNull(color, secondColor).forEachIndexed { index, color ->
            rotate(if (index == 0) 0f else 180f) {
                val brush = Brush.sweepGradient(
                    0f to Color.Transparent,
                    tailToDisplay.value / 360f to color,
                    1f to Color.Transparent
                )
                val paint = setupPaint(strokeStyle, brush)

                drawIntoCanvas { canvas ->
                    canvas.drawArc(
                        rect = size.toRect(),
                        startAngle = 0f,
                        sweepAngle = tailToDisplay.value,
                        useCenter = false,
                        paint = paint
                    )
                }
            }
        }
    }
}

fun DrawScope.setupPaint(
    style: StrokeStyle,
    brush: Brush
): Paint {
    val paint = Paint().apply paint@{
        this@paint.isAntiAlias = true
        this@paint.style = PaintingStyle.Stroke
        this@paint.strokeWidth = style.width.toPx()
        this@paint.strokeCap = style.strokeCap

        brush.applyTo(size, this@paint, 1f)
    }

    style.glowRadius?.let { radius ->
        paint.asFrameworkPaint().setShadowLayer(
            radius.toPx(),
            0f,
            0f,
            android.graphics.Color.WHITE
        )
    }

    return paint
}

@Composable
fun KastLoader(
    modifier: Modifier = Modifier,
    boxSize: Dp = 100.dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (
            modifier = Modifier
                .size(boxSize),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize(0.7f)
                    .border(
                        1.dp,
                        Color.Transparent
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoHeader(
                    textStyle = MaterialTheme.typography.headlineSmall,
                    letterSpacing = 4.sp
                )
            }
            CircleLoader(
                isVisible = true,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun RotatingIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = MaterialTheme.colorScheme.tertiary,
    onClick: () -> Unit = {}
) {
    var isRotating by remember { mutableStateOf(false) }
    var targetValue by remember { mutableFloatStateOf(0f) }

    val rotationDegree = 720f
    val rotationDuration: Int = 2000
    val rotation by animateFloatAsState(
        targetValue = if (isRotating) (targetValue + rotationDegree) else targetValue,
        animationSpec = tween(durationMillis = rotationDuration),
        label = ""
    )

    LaunchedEffect(isRotating) {
        if (isRotating) {
            delay(rotationDuration.toLong())
            isRotating = false
            targetValue += rotationDegree
        }
    }

    Box (
        modifier = modifier
            .clickable {
                isRotating = true
                onClick()
            }
            .rotate(rotation)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Rotating icon",
            modifier = Modifier
                .aspectRatio(1f)
                .border(
                    1.dp,
                    Color.Transparent
                ),
            tint = tint
        )
    }
}