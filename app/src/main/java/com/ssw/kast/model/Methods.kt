package com.ssw.kast.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.media.MediaMetadataRetriever
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat

// ------------- Image methods -----------------

fun getImageFromBase64(base64str: String): ImageBitmap {
    val decodedBytes = Base64.decode(base64str, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    return bitmap.asImageBitmap()
}

fun getImageFromLocalMetadata(audioFilePath: String): ImageBitmap {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(audioFilePath)
    val art = retriever.embeddedPicture
    val bitmap = BitmapFactory.decodeByteArray(art, 0, art?.size ?: 0)
    retriever.release()
    return bitmap.asImageBitmap()
}

@Composable
fun getImageFromResources(resourceId: Int): ImageBitmap {
    val context = LocalContext.current
    val drawable = ResourcesCompat.getDrawable(context.resources, resourceId, null)

    return when (drawable) {
        is BitmapDrawable -> {
            drawable.bitmap.asImageBitmap()
        }
        is VectorDrawable -> {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap.asImageBitmap()
        }
        else -> throw IllegalArgumentException("Unsupported drawable type")
    }
}

// ---------------------------------------------