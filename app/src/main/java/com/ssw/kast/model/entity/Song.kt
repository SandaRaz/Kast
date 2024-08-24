package com.ssw.kast.model.entity

import androidx.compose.ui.graphics.ImageBitmap

class Song {
    lateinit var id: Any
    lateinit var title: String
    lateinit var singer: String
    var year: Int = 0
    var duration: Long = 0
    lateinit var cover: ImageBitmap
    lateinit var coverCode: String
    lateinit var musicGenre: MusicGenre
    lateinit var uploader: User

    constructor(
        id: Any,
        title: String,
        singer: String,
        year: Int,
        duration: Long,
        cover: ImageBitmap,
        coverCode: String,
        musicGenre: MusicGenre,
        uploader: User
    ) {
        this.id = id
        this.title = title
        this.singer = singer
        this.year = year
        this.duration = duration
        this.cover = cover
        this.coverCode = coverCode
        this.musicGenre = musicGenre
        this.uploader = uploader
    }

    constructor(
        title: String,
        singer: String,
        year: Int,
        duration: Long,
        cover: ImageBitmap,
        coverCode: String,
        musicGenre: MusicGenre,
        uploader: User
    ) {
        this.title = title
        this.singer = singer
        this.year = year
        this.duration = duration
        this.cover = cover
        this.coverCode = coverCode
        this.musicGenre = musicGenre
        this.uploader = uploader
    }


}