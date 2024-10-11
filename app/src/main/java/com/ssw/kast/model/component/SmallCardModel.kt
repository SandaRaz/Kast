package com.ssw.kast.model.component

import androidx.compose.ui.graphics.ImageBitmap

class SmallCardModel(
    var image: ImageBitmap,
    var primaryLabel: String,
    var secondaryLabel: String,
    val onClick: () -> Unit = {}
)