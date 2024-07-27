package com.ssw.kast.model.component

import androidx.compose.ui.graphics.ImageBitmap

class SearchResultModel(
    var id: Any,
    var type: String,
    var route: String,
    var image: ImageBitmap,
    var primaryLabel: String,
    var secondLabel: String
)