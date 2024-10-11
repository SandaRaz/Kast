package com.ssw.kast.network.service

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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("country")
    suspend fun getAllCountry(): List<Country>

    @GET("gender")
    suspend fun getAllGender(): List<Gender>

    @GET("musicgenre")
    suspend fun getAllMusicGenre(): List<MusicGenre>

    @GET("user/{userid}")
    suspend fun getUser(@Path("userid") userId: Any): User

    @GET("user/newestusers/{amount}/{userid}")
    suspend fun getNewestUsers(@Path("amount") amount: Int, @Path("userid") userId: Any): List<User>

    @POST("user/haslisten")
    suspend fun hasListenSong(@Body listenDto: Listen): ErrorCatcher

    @POST("auth/validage/{dateofbirth}")
    suspend fun isValidAge(@Path("dateofbirth") dateOfBirth: String): ErrorCatcher

    @POST("auth/validusermail/{usermail}")
    suspend fun isValidUsermail(@Path("usermail") usermail: String): ErrorCatcher

    @POST("auth/validusername/{username}")
    suspend fun isValidUsername(@Path("username") username: String): ErrorCatcher

    @POST("auth/signin")
    suspend fun signIn(@Body userCredential: AuthInfo): User

    @POST("auth/signout")
    suspend fun signOut(@Body userId: Any): ErrorCatcher

    @POST("auth/signup")
    suspend fun signUp(@Body user: User): ErrorCatcher

// ------ Follow services ------

    @POST("user/follow")
    suspend fun follow(@Body followerDto: FollowDto): ErrorCatcher

    @POST("user/isfollowing")
    suspend fun isFollowing(@Body followerDto: FollowDto): Boolean

    @POST("user/unfollow")
    suspend fun unfollow(@Body unfollowDto: UnfollowDto): ErrorCatcher

// ---- End follow services ----

    /*
    ----- Synchronous alternative (must be run inside coroutines or
    dispatched to side threads) -----
    @POST("auth/signup")
    fun signUp(@Body user: User): Call<ErrorCatcher>

     */
}