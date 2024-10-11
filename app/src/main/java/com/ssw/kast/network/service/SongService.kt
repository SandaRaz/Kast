package com.ssw.kast.network.service

import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.view.TopCategory
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SongService {
    @Deprecated("Does not match kastcore endpoint anymore")
    @GET("song/stream/{songid}")
    suspend fun getAudioStream(@Path("songid", encoded = true) songId: Any): Response<ResponseBody>

    @GET("song/{userid}/{musicgenreid}/{offset}/{limit}")
    suspend fun getMusicGenreSongs(
        @Path("userid") userid: Any,
        @Path("musicgenreid") musicgenreid: Any,
        @Path("offset") offset: Int,
        @Path("limit") limit: Int
    ): List<Song>

    @GET("song/suggestions/{userid}")
    suspend fun getSuggestedSongs(@Path("userid") userId: Any): List<Song>

    @GET("song/topcategories")
    suspend fun getTopCategories(): List<TopCategory>

    @GET("song/moststreamed/{amount}")
    suspend fun mostStreamedSongs(@Path("amount") amount: Int): List<Song>

    @GET("example") // exemple : http://localhost:8080/example?id=7001
    suspend fun getExampleById(@Query("id") id: Int): List<Song>
}
