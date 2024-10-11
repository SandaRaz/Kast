package com.ssw.kast.network.repository

import com.ssw.kast.model.entity.Country
import com.ssw.kast.model.entity.Gender
import com.ssw.kast.model.entity.MusicGenre
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.dto.AuthInfo
import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.dto.FollowDto
import com.ssw.kast.model.dto.PlaylistDto
import com.ssw.kast.model.dto.UnfollowDto
import com.ssw.kast.model.entity.Listen
import com.ssw.kast.model.entity.Playlist
import com.ssw.kast.network.service.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {
    suspend fun getAllCountry(): List<Country> = userService.getAllCountry()
    suspend fun getAllGender(): List<Gender> = userService.getAllGender()
    suspend fun getAllMusicGenre(): List<MusicGenre> = userService.getAllMusicGenre()
    suspend fun getUser(userId: Any): User = userService.getUser(userId)
    suspend fun getNewestUsers(amount: Int, userId: Any): List<User> = userService.getNewestUsers(amount, userId)

    suspend fun hasListenSong(listenDto: Listen): ErrorCatcher = userService.hasListenSong(listenDto)

    suspend fun isValidAge(dateOfBirth: String): ErrorCatcher = userService.isValidAge(dateOfBirth)
    suspend fun isValidUsermail(usermail: String): ErrorCatcher = userService.isValidUsermail(usermail)
    suspend fun isValidUsername(username: String): ErrorCatcher = userService.isValidUsername(username)

    suspend fun signIn(userCredential: AuthInfo): User = userService.signIn(userCredential)
    suspend fun signOut(userId: Any): ErrorCatcher = userService.signOut(userId)
    suspend fun signUp(user: User): ErrorCatcher = userService.signUp(user)

    suspend fun follow(followDto: FollowDto): ErrorCatcher = userService.follow(followDto)
    suspend fun isFollowing(followDto: FollowDto): Boolean = userService.isFollowing(followDto)
    suspend fun unfollow(unFollowDto: UnfollowDto): ErrorCatcher = userService.unfollow(unFollowDto)

}