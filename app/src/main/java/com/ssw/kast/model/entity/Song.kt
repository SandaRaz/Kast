package com.ssw.kast.model.entity

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import com.google.gson.annotations.JsonAdapter
import com.ssw.kast.model.global.getImageFromBase64
import com.ssw.kast.model.persistence.dao.SongDao
import com.ssw.kast.model.serializer.DateUtils
import com.ssw.kast.model.serializer.DtoTS
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import java.util.UUID

class Song {
    var id: Any = UUID.randomUUID()
    var title: String = ""
    var singer: String = ""
    var year: Int = 0
    @JsonAdapter(DtoTS::class)
    lateinit var duration: Duration
    var localPath: String = ""
    var cover: ImageBitmap? = null
    var coverCode: String = ""
    var albumId: Any = ""
    var album: Album? = null
    var musicGenreId: Any = ""
    var musicGenre: MusicGenre? = null
    lateinit var uploaderId: Any
    var state: Int = 5
    lateinit var uploadAt: LocalDateTime
    var listeners: Int = 0

    constructor()

    constructor(
        id: Any,
        title: String,
        singer: String,
        year: Int,
        duration: Duration,
        cover: ImageBitmap?,
        coverCode: String,
        musicGenre: MusicGenre?
    ) {
        this.id = id
        this.title = title
        this.singer = singer
        this.year = year
        this.duration = duration
        this.cover = cover
        this.coverCode = coverCode
        this.musicGenre = musicGenre
    }

    fun getCover(default: ImageBitmap): ImageBitmap {
        return this.cover ?: default
    }

    fun loadCoverBitmap() {
        try {
            if (this.coverCode.isNotBlank()) {
                this.cover = getImageFromBase64(this.coverCode)
            }
        } catch (e: Exception) {
            Log.e("LoadBitmap", "Exception for Song ${this.title}: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadCoverBitmapAndEmptyCoverCode() {
        try {
            if (this.coverCode.isNotBlank()) {
                this.cover = getImageFromBase64(this.coverCode)
                this.coverCode = ""
            }
        } catch (e: Exception) {
            Log.e("LoadBitmap", "Exception for Song ${this.title}: ${e.message}")
            e.printStackTrace()
        }
    }

    companion object {
        fun getSong(songDao: SongDao): Song {
            val song = Song()
            song.id = songDao.id
            song.title = songDao.title
            song.singer = songDao.singer
            song.year = songDao.year
            song.duration = DateUtils.csharpTimeSpanToDuration(songDao.duration)
            song.localPath = songDao.localPath
            song.coverCode = songDao.coverCode
            song.loadCoverBitmap()
            song.albumId = songDao.albumId
            song.album = Album(songDao.albumId, songDao.albumName)
            song.musicGenreId = songDao.musicGenreId
            song.musicGenre = MusicGenre(songDao.musicGenreId, songDao.musicGenreType)
            song.uploaderId = songDao.uploaderId
            song.state = songDao.state
            song.uploadAt = DateUtils.csharpDateTimeToLocalDateTime(songDao.uploadAt)
            song.listeners = songDao.listeners

            return song
        }

        fun getSongDao(song: Song, dataType: Int): SongDao {
            val songDao = SongDao(
                id = song.id.toString(),
                title = song.title,
                singer = song.singer,
                year = song.year,
                duration = DateUtils.durationToCsharpTimeSpan(song.duration),
                localPath = song.localPath,
                coverCode = song.coverCode,
                albumId = song.albumId.toString(),
                albumName = song.album?.name ?: "unknown",
                musicGenreId = song.musicGenreId.toString(),
                musicGenreType = song.musicGenre?.type ?: "undefined",
                uploaderId = song.uploaderId.toString(),
                listeners = song.listeners,
                state = song.state,
                uploadAt = DateUtils.localDateTimeToCsharpDateTime(song.uploadAt),
                dataType = dataType, /* exemple: recent = 2 */
                date = DateUtils.localDateTimeToCsharpDateTime(LocalDateTime.now())
            )

            return songDao
        }

        fun getSongs(songsDao: List<SongDao>): List<Song> {
            val songs: MutableList<Song> = mutableListOf()
            for (songDao in songsDao) {
                songs.add(Song.getSong(songDao))
            }

            return songs
        }


        fun getTestSong(): Song {
            val user = User("ADM0001","Administrator")

            val song = Song (
                id = "TEST001",
                title = "Ne viens pas",
                singer = "Roch Voisine",
                year = 2005,
                duration = Duration.ofMinutes(3),
                cover = null,
                coverCode = "",
                musicGenre = null
            )

            return song
        }

        fun modifyExtensionToHLS(songPath: String): String {
            return songPath.replaceAfterLast(".", "m3u8")
        }
    }
}