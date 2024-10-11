package com.ssw.kast.model.component

import androidx.compose.ui.graphics.ImageBitmap

class MusicCardModel(
    var title: String,
    var description: String? = null,
    var description2: String? = null,
    var image: ImageBitmap,
    val onClick: () -> Unit = {}
)