package com.ssw.kast.model.view

class TopCategory {
    var musicGenreId: Any = ""
    var musicGenreType: String = ""
    var value: Int = 0

    constructor()

    constructor(musicGenreId: Any, musicGenreType: String, value: Int) {
        this.musicGenreId = musicGenreId
        this.musicGenreType = musicGenreType
        this.value = value
    }
}