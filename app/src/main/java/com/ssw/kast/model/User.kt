package com.ssw.kast.model

import androidx.compose.ui.graphics.ImageBitmap
import org.threeten.bp.LocalDate

class User {
    lateinit var id: Any
    var username: String
    var usermail: String
    lateinit var passwordHash: String
    lateinit var dateOfBirth: LocalDate
    var profilePicture: ImageBitmap
    var biography: String = ""
    var followersCount: Int = 0
    var followingCount: Int = 0
    var createdAt: LocalDate = LocalDate.now()
    lateinit var gender: Gender
    lateinit var country: Country
    lateinit var userMusicGenres: List<MusicGenre>

    constructor(
        id: Any,
        username: String,
        usermail: String,
        passwordHash: String,
        dateOfBirth: LocalDate,
        profilePicture: ImageBitmap,
        biography: String,
        followersCount: Int,
        followingCount: Int,
        createdAt: LocalDate,
        gender: Gender,
        country: Country
    ) {
        this.id = id
        this.username = username
        this.usermail = usermail
        this.passwordHash = passwordHash
        this.dateOfBirth = dateOfBirth
        this.profilePicture = profilePicture
        this.biography = biography
        this.followersCount = followersCount
        this.followingCount = followingCount
        this.createdAt = createdAt
        this.gender = gender
        this.country = country
    }

    constructor(username: String, usermail: String, profilePicture: ImageBitmap) {
        this.username = username
        this.usermail = usermail
        this.profilePicture = profilePicture
    }
}