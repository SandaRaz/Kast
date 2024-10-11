package com.ssw.kast.model.entity

import com.ssw.kast.model.dto.PlaylistDto
import org.threeten.bp.LocalDateTime

class Playlist {
    var id: Any = ""
    var name: String = ""
    var createdAt: LocalDateTime = LocalDateTime.now()
    var userId: Any = ""
    var user: User = User()
    var songs: List<Song> = emptyList()

    constructor()

    constructor(id: Any, name: String, createdAt: LocalDateTime, userId: Any) {
        this.id = id
        this.name = name
        this.createdAt = createdAt
        this.userId = userId
    }

    companion object {
        fun getPlaylist(playlistDto: PlaylistDto): Playlist {
            val playlist = Playlist(
                id = playlistDto.id,
                name = playlistDto.name,
                createdAt = playlistDto.createdAt,
                userId = playlistDto.userId
            )

            return playlist
        }
    }
}