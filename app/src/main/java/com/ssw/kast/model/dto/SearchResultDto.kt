package com.ssw.kast.model.dto

import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User

class SearchResultDto {
    var songs: List<Song> = emptyList()
    var users: List<User> = emptyList()

    constructor()

    constructor(songs: List<Song>, users: List<User>) {
        this.songs = songs
        this.users = users
    }

    fun empty() {
        this.songs = emptyList()
        this.users = emptyList()
    }
}