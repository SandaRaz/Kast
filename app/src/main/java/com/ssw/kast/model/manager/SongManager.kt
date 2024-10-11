package com.ssw.kast.model.manager

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.ssw.kast.model.dto.ListeningData
import com.ssw.kast.model.entity.MusicGenre
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.model.player.AudioPlayer
import com.ssw.kast.model.view.TopCategory
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit

class SongManager {
    private lateinit var database: AppDatabase
    private lateinit var context: Context
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var hubConnection: HubConnection
    private lateinit var baseUrl: String
    private lateinit var hubName: String
    lateinit var audioPlayer: AudioPlayer

    var currentSong: Song? by mutableStateOf(null)
    var isPlayed: Boolean by mutableStateOf(false)
    val songLoading = mutableStateOf(false)

    var songList = mutableStateOf<List<Song>>(emptyList())
    val recentSongs = mutableStateListOf<Song>()

    var canSaveSongToListened by mutableStateOf(false)
    var timeListenedCurrentSong by mutableLongStateOf(0L) /* represent second */
    val listenedSongs = mutableStateListOf<ListeningData>()

    var currentCategory: TopCategory? by mutableStateOf(null)

    val onSongListChange: (List<Song>) -> Unit = {
        songList.value = it
    }

    constructor()

    constructor(currentSong: Song?) {
        this.currentSong = currentSong
    }

    constructor(
        database: AppDatabase,
        context: Context,
        preferencesManager: PreferencesManager,
        currentSong: Song?
    ) {
        this.database = database
        this.context = context
        this.preferencesManager = preferencesManager
        this.currentSong = currentSong

        val protocol = preferencesManager.getStringData("protocol", "http")
        val ipAddress = preferencesManager.getStringData("ipAddress", "10.0.2.2")
        val port = preferencesManager.getStringData("port", "5039")

        this.baseUrl = "$protocol://$ipAddress:$port"
        this.hubName = "kasthub"

        this.audioPlayer = AudioPlayer(this.context,this)
    }

    // ------ Listen functions ------

    fun addCurrentSongToListened() {
        listenedSongs.add(
            ListeningData(
                songId = currentSong?.id!!,
                start = LocalDateTime.now()
            )
        )
        Log.d("AddSongToListened", "Listened ${currentSong?.title}")
    }

    // ---- End listen functions ----

    suspend fun addToRecent(song: Song, userId: Any) {
        if (recentSongs.size == 15) {
            recentSongs.removeFirst()
            database.recentSongDao().deleteOldestRecentSong(userId.toString())
        }

        for (recentSong in recentSongs) {
            if (recentSong.id == song.id) {
                recentSongs.remove(recentSong)
                database.recentSongDao().deleteRecentSong(recentSong.id.toString(), userId.toString())
                break
            }
        }
        recentSongs.add(song)
        database.recentSongDao().insert(Song.getSongDao(song, 2, userId.toString()))
        //Log.d("SongManager", "Add song in recent, size = ${recentSongs.size}")
    }

    fun clickCurrentSong() {
        if (this.isPlayed) {
            pauseSong()
        } else {
            playSong(false)
        }
        this.isPlayed = !this.isPlayed
    }

    suspend fun clickNewSong(song: Song, userId: Any, isPlayed: Boolean) {
        if (!isCurrent(song)) {
            this.songLoading.value = true

            this.currentSong = song
            //Log.d("SongManager","CurrentSong Id: ${this.currentSong!!.id}")
            this.isPlayed = isPlayed
            this.addToRecent(song, userId)

            // ---- Restore player's song position ----
            this.audioPlayer.player.seekTo(0L)

            playSong()
        } else {
            if (!isPlayed) {
                this.isPlayed = true
                playSong(false)
            }
        }
    }

    private fun getIndexFromId(id: Any, list: List<Song>): Int {
        var index = -1
        for (i in list.indices) {
            if (list[i].id == id) {
                index = i
                break
            }
        }
        return index
    }

    fun isCurrent(song: Song): Boolean {
        currentSong?.let { currentSong ->
            if (currentSong.id == song.id) {
                return true
            }
        }
        return false
    }

    fun nextSong(list: List<Song>) {
        val indexSelected = getIndexFromId(currentSong?.id!!, list)
        //Log.d("SongManager","Next song, index = ${indexSelected + 1}")
        if (indexSelected + 1 < list.size) {
            this.currentSong = list[indexSelected + 1]
            this.isPlayed = true
        } else {
            this.currentSong = list.first()
            this.isPlayed = true
        }

        stopSong()
        playSong()
    }

    fun previousSong(list: List<Song>) {
        val indexSelected = getIndexFromId(currentSong?.id!!, list)
        //Log.d("SongManager","Previous song, index = ${indexSelected - 1}")

        if (indexSelected - 1 >= 0) {
            this.currentSong = list[indexSelected - 1]
            this.isPlayed = true
        } else {
            this.currentSong = list.last()
            this.isPlayed = true
        }

        stopSong()
        playSong()
    }

    // ------- Stream features -------

    fun connectToAudioHub(songPath: String) {
        hubConnection = HubConnectionBuilder.create("$baseUrl/$hubName").build()
        hubConnection.on(
            "ReceiveAudioChunk",
            { data: ByteArray, bytesRead: Int ->
                Log.d("SongManager", "Data Length: ${data.size}")
                Log.d("SongManager", "*** PlayAudioChunk ***")
                playAudioChunk(data, bytesRead)
            },
            ByteArray::class.java, Int::class.java
        )
        hubConnection.start().doOnComplete {
            hubConnection.send("StartStreaming", songPath)
            Log.d("HubConnection", "Hub Connection Success")
        }.doOnError { throwable ->
            Log.e("HubConnection", "Hub Connection Error: ${throwable.message}")
            throwable.printStackTrace()
        }.blockingAwait()
    }

    private fun playAudioChunk(data: ByteArray, bytesRead: Int) {
        audioPlayer.playAudioChunk(data, bytesRead)
    }

    fun playSong(newSong: Boolean = true) {
        val currentMediaItem = audioPlayer.player.currentMediaItem

        //Log.d("SongManager", "Current Media Item: $currentMediaItem")
        //Log.d("SongManager", "Current Position: $currentPosition")

        if ((currentMediaItem != null) && !newSong) {
            resumeSong()
        } else {
            // ------- Play new song -------
            this.canSaveSongToListened = true
            this.timeListenedCurrentSong = 0L

            audioPlayer.playHlsAudio(this.baseUrl, this.currentSong!!.id)
        }

    }

    fun resumeSong() {
        //Log.d("SongManager", "****************** RESUME SONG *****************")
        audioPlayer.player.play()
    }

    fun pauseSong() {
        audioPlayer.player.pause()
    }

    fun stopSong() {
        audioPlayer.player.stop()
    }

    fun releaseSong() {
        audioPlayer.player.release()
    }

    fun seekForward() {
        //Log.d("SongManager","CurrentPosition: ${audioPlayer.player.currentPosition}")
        audioPlayer.player.seekTo(audioPlayer.player.currentPosition + 5000)
    }

    fun seekBackward() {
        //Log.d("SongManager","CurrentPosition: ${audioPlayer.player.currentPosition}")
        if ((audioPlayer.player.currentPosition - 5000) >= 0) {
            //Log.d("SongManager","Seek backward")
            audioPlayer.player.seekTo(audioPlayer.player.currentPosition - 5000)
        } else {
            //Log.d("SongManager","Seek forward")
            audioPlayer.player.seekTo(0)
        }
    }

    // ----- End stream features -----

    // -------- Dao functions --------

    suspend fun loadRecentSong(userId: Any) {
        try {
            if (this.recentSongs.size == 0) {
                refreshRecentSong(userId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Load Recent Song", "Exception: ${e.message}")
        }
    }

    suspend fun refreshRecentSong(userId: Any) {
        try {
            val recentSongsDao = this.database.recentSongDao().getRecentSongs(userId.toString())
            //Log.d("Load Recent Song", "Recent song in database: ${recentSongsDao.size}")
            this.recentSongs.clear()
            for(songDao in recentSongsDao) {
                this.recentSongs.add(Song.getSong(songDao))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Refresh Recent Song", "Exception: ${e.message}")
        }
    }

    // ------ End dao functions ------
}