package com.ssw.kast.model.component

import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.manager.SongManager

class MusicListModel(
    var songManager: SongManager,
    var song: Song,
    val onClick: () -> Unit,
    var isFavorite: Boolean = false,
    val addToFavorite: () -> Unit
)