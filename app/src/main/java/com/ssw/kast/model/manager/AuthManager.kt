package com.ssw.kast.model.manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ssw.kast.model.entity.MusicGenre
import com.ssw.kast.model.entity.User

class AuthManager {
    var newUser by mutableStateOf(User())
    var newUserMusicGenres by mutableStateOf<List<MusicGenre>>(emptyList())

    constructor()

    constructor(newUser: User) {
        this.newUser = newUser
    }

    constructor(newUser: User, newUserMusicGenres: List<MusicGenre>) {
        this.newUser = newUser
        this.newUserMusicGenres = newUserMusicGenres
    }

    fun reinitialize() {
        this.newUser = User()
        this.newUserMusicGenres = emptyList()
    }
}