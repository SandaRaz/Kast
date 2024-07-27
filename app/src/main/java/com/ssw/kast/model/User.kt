package com.ssw.kast.model

import android.text.format.DateUtils
import androidx.compose.ui.graphics.ImageBitmap
import java.util.Date

class User {
    lateinit var id: Any
    var username: String
    var usermail: String
    lateinit var passwordHash: String
    lateinit var dateOfBirth: Date
    var profilePicture: ImageBitmap
    var biography: String = ""
    var followersCount: Int = 0
    var followingCount: Int = 0
    var createdAt: Date = Date()
    lateinit var gender: Gender
    lateinit var country: Country

    constructor(
        id: Any,
        username: String,
        usermail: String,
        passwordHash: String,
        dateOfBirth: Date,
        profilePicture: ImageBitmap,
        biography: String,
        followersCount: Int,
        followingCount: Int,
        createdAt: Date,
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