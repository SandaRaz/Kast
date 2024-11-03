package com.ssw.kast.model.global

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.icu.text.SimpleDateFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentResolverCompat
import androidx.core.content.res.ResourcesCompat
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.util.Locale
import kotlin.experimental.and

// ------------- Image methods -----------------

fun getBase64FromUri(context: Context, imageUri: Uri): String {
    try {
        val bytes = context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val buffer = ByteArray(1024)
            val output = ByteArrayOutputStream()
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
            output.toByteArray()
        }

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

val resourceImageCache = mutableMapOf<Int, ImageBitmap>()
val base64ImageCache = mutableMapOf<String, ImageBitmap>()

fun clearCache() {
    resourceImageCache.clear()
}

@Composable
fun getCachedImageFromResources(resourceId: Int): ImageBitmap {
    //Log.d("Image Methods", "Resource image cache size: ${resourceImageCache.size}")
    if (resourceImageCache.size >= 30) {
        clearCache()
        //Log.d("Image Methods", "Resource image cache size now: ${resourceImageCache.size}")
    }

    resourceImageCache[resourceId]?.let {
        return it
    }

    val context = LocalContext.current
    val imageBitmap = loadImageFromResources(context, resourceId)
    resourceImageCache[resourceId] = imageBitmap
    return imageBitmap
}

@Composable
fun getCachedImageFromResourcesOld(resourceId: Int): ImageBitmap {
    val context = LocalContext.current
    val imageBitmap by remember(resourceId) {
        mutableStateOf(loadImageFromResources(context, resourceId))
    }
    return imageBitmap
}

fun getImageFromBase64(base64str: String): ImageBitmap {
//    Log.d("Base64_ImageCache", "Base64 image cache size: ${base64ImageCache.size}")
    base64ImageCache[base64str]?.let {
        return it
    }
    val decodedBytes = Base64.decode(base64str, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    val imageBitmap = bitmap.asImageBitmap()
    base64ImageCache[base64str] = imageBitmap
    return imageBitmap
}

fun getImageFromBase64V2(base64str: String): ImageBitmap {
    val decodedBytes = Base64.decode(base64str, Base64.DEFAULT)
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, options)
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

fun loadImageFromResources(context: Context, resourceId: Int): ImageBitmap {
    val drawable = ResourcesCompat.getDrawable(context.resources, resourceId, null)
    return when (drawable) {
        is BitmapDrawable ->
            drawable.bitmap.asImageBitmap()
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
        } else ->
            throw IllegalArgumentException("Unsupported drawable type")
    }
}

// ----------- End image methods ---------------

// -------------- Date functions ---------------

fun dateFromString(dateText: String): LocalDate {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val date: LocalDate = LocalDate.parse(dateText, formatter)

    return date
}

fun dateToString(date: LocalDate): String {
    if (date == LocalDate.MIN) {
        return ""
    } else {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val dateString = date.format(formatter)

        return dateString
    }
}

// ------------ End date functions -------------

// -------------- Color functions --------------

fun Color.toLong(): Long {
    return (this.value.toByte() and 0xFFFFFFF.toByte()).toLong()
}

fun Long.toColor(): Color {
    return Color(this)
}

// ---------------------------------------------