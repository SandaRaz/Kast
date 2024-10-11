package com.ssw.kast.model.component

import androidx.compose.ui.graphics.ImageBitmap

class ListCardModel(
    var title: String,
    var description: String? = null,
    var image: ImageBitmap?,
    val onClick: () -> Unit
)