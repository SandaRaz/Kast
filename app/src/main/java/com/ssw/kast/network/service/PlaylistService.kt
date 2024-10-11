package com.ssw.kast.network.service

import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.dto.PlaylistDto
import com.ssw.kast.model.entity.Playlist
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlaylistService {
    @POST("user/playlist/{playlistid}/{songid}")
    suspend fun addSongToPlaylist(@Path("playlistid") playlistid: Any, @Path("songid") songid: Any): ErrorCatcher

    @POST("user/playlist")
    suspend fun createPlaylist(@Body playlist: PlaylistDto): ErrorCatcher

    @DELETE("user/playlist/{playlistid}")
    suspend fun deletePlaylist(@Path("playlistid") playlistId: Any): ErrorCatcher

    @GET("user/playlist/{playlistid}")
    suspend fun getPlaylist(@Path("playlistid") playlistId: Any): Playlist

    @GET("user/{userid}/playlist")
    suspend fun getPlaylists(@Path("userid") userId: Any): List<Playlist>

    @DELETE("user/playlist/{playlistid}/{songid}")
    suspend fun removeSongFromPlaylist(@Path("playlistid") playlistId: Any, @Path("songid") songId: Any): ErrorCatcher

    @PUT("user/{userid}/playlist/{playlistid}/{newname}")
    suspend fun renamePlaylist(@Path("userid") userId: Any, @Path("playlistid") playlistId: Any, @Path("newname") newName: String): ErrorCatcher
}