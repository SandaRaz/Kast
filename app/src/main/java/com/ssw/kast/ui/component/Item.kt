package com.ssw.kast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    containerShape: Shape = RoundedCornerShape(12.dp),
    onClick: () -> Unit = {}
) {
    val cornerShape = 12.dp

    Margin (
        top = 8.dp,
        start = 0.dp,
        end = 0.dp,
        bottom = 0.dp
    ) {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = containerShape
                )
                .background(
                    color = containerColor,
                    shape = containerShape
                )
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .border(1.dp, Color.Transparent)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(16.dp)
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )
        }
    }
}