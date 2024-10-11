package com.ssw.kast.model.entity

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import com.ssw.kast.model.global.getImageFromBase64
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.dto.AuthInfo
import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.dto.FollowDto
import com.ssw.kast.model.dto.UnfollowDto
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.model.persistence.dao.UserDao
import com.ssw.kast.model.serializer.DateUtils
import com.ssw.kast.network.repository.UserRepository
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.UUID

class User {
    var id: Any = UUID.randomUUID()
    var username: String = ""
    var usermail: String = ""
    var passwordHash: String = ""
    var dateOfBirth: LocalDate = LocalDate.now()
    var profilePicture: ImageBitmap? = null
    var profilePictureCode: String = ""
    var biography: String = ""
    var followersCount: Int = 0
    var followingCount: Int = 0
    var createdAt: LocalDateTime = LocalDateTime.now()
    var state: Int = 1

    var genderId: Any = ""
    var gender: Gender = Gender()

    var countryId: Any = ""
    var country: Country = Country()

    var statusId: Any = UUID.fromString("143150b4-0049-4282-932e-5c0b0409ced2")
    var status: Status = Status()

    var errorCatcher: ErrorCatcher = ErrorCatcher()

    var musicGenres: List<MusicGenre> = mutableListOf()
    var songs: List<Song> = mutableListOf()
    var favorites: List<Favorite> = mutableListOf()
    var playlists: List<Playlist> = mutableListOf()
    var userActivities: List<UserActivity> = mutableListOf()

    constructor()

    constructor(
        id: Any,
        username: String,
        usermail: String,
        passwordHash: String,
        dateOfBirth: LocalDate,
        profilePicture: ImageBitmap?,
        profilePictureCode: String,
        biography: String,
        followersCount: Int,
        followingCount: Int,
        createdAt: LocalDateTime,
        gender: Gender,
        country: Country
    ) {
        this.id = id
        this.username = username
        this.usermail = usermail
        this.passwordHash = passwordHash
        this.dateOfBirth = dateOfBirth
        this.profilePicture = profilePicture
        this.profilePictureCode = profilePictureCode
        this.biography = biography
        this.followersCount = followersCount
        this.followingCount = followingCount
        this.createdAt = createdAt
        this.gender = gender
        this.country = country
    }

    constructor(username: String, usermail: String, profilePicture: ImageBitmap?) {
        this.username = username
        this.usermail = usermail
        this.profilePicture = profilePicture
    }

    constructor(id: Any, username: String) {
        this.id = id
        this.username = username
    }

    override fun toString(): String {
        val musicGenres = mutableListOf<String>()
        this.musicGenres.forEach {
            musicGenres.add(it.type)
        }

        val str = "Id: $id \n Username: $username \n " +
                "Usermail: $usermail \n DateOfBirth: $dateOfBirth \n " +
                "CountryId: $countryId \n GenderId: $genderId \n " +
                "CreatedAt: $createdAt \n Status: ${status.label}" +
                "MusicGenres: {${musicGenres}}"
        return str
    }

    fun loadBitmapProfilePicture() {
        try {
            if (this.profilePictureCode.isNotBlank()) {
                this.profilePicture = getImageFromBase64(this.profilePictureCode)
                this.profilePictureCode = ""
            }
        } catch (e: Exception) {
            Log.e("LoadBitmap", "Exception for User ${this.username}: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadBitmapProfilePicture(defaultImage: ImageBitmap) {
        try {
            if (this.profilePictureCode.isNotBlank()) {
                this.profilePicture = getImageFromBase64(this.profilePictureCode)
                this.profilePictureCode = ""
            } else {
                this.profilePicture = defaultImage
            }
        } catch (e: Exception) {
            Log.e("LoadBitmap", "Exception for User ${this.username}: ${e.message}")
            e.printStackTrace()
        }
    }

    // ------- Suspendable functions -------

    suspend fun follow(
        userRepository: UserRepository,
        followedId: Any
    ): ErrorCatcher {
        var err = ErrorCatcher()
        try {
            val followDto = FollowDto(this.id, followedId)
            err = userRepository.follow(followDto)
        } catch (e: Exception) {
            err.error = "${e.message}"
            err.code = 1

            Log.d("User follow", "Exception: ${e.message}")
            e.printStackTrace()
        }

        return err
    }

    suspend fun hasListenSong(
        userRepository: UserRepository,
        songId: Any,
        preciseTime: LocalDateTime
    ): ErrorCatcher {
        var err = ErrorCatcher()

        try {
            val listenDto = Listen(
                id = UUID.randomUUID(),
                songId = songId,
                listenerId = this.id,
                preciseDate = preciseTime
            )
            err = userRepository.hasListenSong(listenDto)

            if (err.code != 0) throw Exception(err.error)
        } catch (e: Exception) {
            err.error = "${e.message}"
            err.code = 1

            Log.d("User HasListen", "Exception: ${e.message}")
            e.printStackTrace()
        }

        return err
    }

    suspend fun isFollowing(
        userRepository: UserRepository,
        followedId: Any
    ): Boolean {
        var isFollowing = false
        try {
            val followDto = FollowDto(this.id, followedId)
            isFollowing = userRepository.isFollowing(followDto)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("User isFollow", "Exception: ${e.message}")
        }

        return isFollowing
    }

    suspend fun signUp(
        database: AppDatabase,
        userRepository: UserRepository,
        accountManager: AccountManager
    ): ErrorCatcher {
        var err = ErrorCatcher()
        try {
            err = userRepository.signUp(this)

            if (err.code == 0) {
                val userDao = getUserDao(this)
                database.userDao().insert(userDao)

                this.loadBitmapProfilePicture()
                accountManager.currentUser = this
            } else {
                throw Exception("Error code ${err.code}: ${err.error}")
            }
        } catch (e: Exception) {
            err.error = "${e.message}"
            err.code = 1

            e.printStackTrace()
            Log.d("User sign up", "Exception: ${e.message}")
        }

        return err
    }

    suspend fun unfollow(
        userRepository: UserRepository,
        unfollowedId: Any
    ): ErrorCatcher {
        var err = ErrorCatcher()
        try {
            val unfollowDto = UnfollowDto(this.id, unfollowedId)
            err = userRepository.unfollow(unfollowDto)
        } catch (e: Exception) {
            err.error = "${e.message}"
            err.code = 1

            Log.d("User follow", "Exception: ${e.message}")
            e.printStackTrace()
        }

        return err
    }

    // ----- End suspendable functions -----

    companion object {
        suspend fun getUser(
            userRepository: UserRepository,
            userId: Any
        ): User {
            var user = User()
            try {
                user = userRepository.getUser(userId)

                if (user.errorCatcher.code != 0) {
                    throw Exception(user.errorCatcher.error)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("User get", "${e.message}")
            }

            return user;
        }

        fun getUser(userDao: UserDao): User {
            val user = User()
            user.id = userDao.id
            user.username = userDao.username
            user.usermail = userDao.usermail
            user.passwordHash = userDao.passwordHash
            user.dateOfBirth = DateUtils.csharpDateTimeToLocalDate(userDao.dateOfBirth)
            user.profilePictureCode = userDao.profilePictureCode
            user.biography = userDao.biography
            user.followersCount = userDao.followersCount
            user.followingCount = userDao.followingCount
            user.createdAt = DateUtils.csharpDateTimeToLocalDateTime(userDao.createdAt)
            user.state = userDao.state
            user.gender = Gender(userDao.genderId, userDao.genderType)
            user.country = Country(userDao.countryId, userDao.countryName)
            user.status = Status(userDao.statusId, userDao.statusLabel, userDao.statusRank)

            return user
        }

        fun getUserDao(user: User): UserDao {

            val userDao = UserDao(
                id = user.id.toString(),
                username = user.username,
                usermail = user.usermail,
                passwordHash = user.passwordHash,
                dateOfBirth = DateUtils.localDateToCsharpDateTime(user.dateOfBirth),
                profilePictureCode = user.profilePictureCode,
                biography = user.biography,
                followersCount = user.followersCount,
                followingCount = user.followingCount,
                createdAt = DateUtils.localDateTimeToCsharpDateTime(user.createdAt),
                state = user.state,
                genderId = user.genderId as String,
                genderType = user.gender.type,
                countryId = user.countryId as String,
                countryName = user.country.name,
                statusId = user.statusId.toString(),
                statusLabel = user.status.label,
                statusRank = user.status.rank
            )

            return userDao
        }

        // ------- Suspendable companion object -------

        suspend fun signIn(userRepository: UserRepository, userCredential: AuthInfo): User {
            var user = User()
            try {
                user = userRepository.signIn(userCredential)

            } catch (e: Exception) {
                e.printStackTrace()
                user.errorCatcher = ErrorCatcher("Exception: ${e.message}", 1)
                Log.e("User sign in", "Exception: ${e.message}")
            }

            return user
        }

        suspend fun signOut(database: AppDatabase, userRepository: UserRepository, accountManager: AccountManager): Boolean {
            var success = false

            try {
                val tableRow = database.userDao().countUsers()
                if (tableRow > 0 && accountManager.currentUser != null) {
                    val currentUser = accountManager.currentUser!!

                    val err = userRepository.signOut(currentUser.id)
                    success = err.code == 0

                    if (success) {
                        val afterSignOutTableRow = database.userDao().emptyUsers()
                        Log.d("SignInViewModel", "User table row: $afterSignOutTableRow")
                        accountManager.currentUser = null
                    }
                }
            } catch (e: Exception) {
                Log.e("User sign out", "Exception: ${e.message}")
            }

            return success
        }

        // ----- End suspendable companion object -----
    }
}