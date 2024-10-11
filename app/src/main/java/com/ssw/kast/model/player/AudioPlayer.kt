package com.ssw.kast.model.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.ssw.kast.model.manager.SongManager

class AudioPlayer {
    lateinit var context: Context
    lateinit var player: ExoPlayer
    private lateinit var songManager: SongManager
    private val byteBuffer = mutableListOf<Byte>()

    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null

    constructor()

    constructor(context: Context, songManager: SongManager) {
        this.context = context
        this.songManager = songManager

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS * 2,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS * 2
            )
            .build()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        this.player = ExoPlayer.Builder(this.context)
            .setAudioAttributes(audioAttributes, true)
            .setLoadControl(loadControl)
            .build()

        player.addListener(object: Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e("PlayerError", "Error occured: ${error.message}")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    startListeningForPositionUpdate()
                } else {
                    stopListeningForPositionUpdate()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        Log.d("PlayerState", "Ready to play")
                        songManager.songLoading.value = false
                    }
                    Player.STATE_ENDED -> {
                        Log.d("PlayerState", "Playback endend")
                        songManager.nextSong(songManager.songList.value)
                    }
                    Player.STATE_BUFFERING -> {
                        Log.d("PlayerState", "Buffering")
                    }
                    Player.STATE_IDLE -> {
                        Log.d("PlayerState", "Player is idle")
                    }
                }
            }
        })
    }

    fun playHlsAudio(baseUrl: String, songId: Any, value: String = "default") {
        val fullUrl = "$baseUrl/song/stream/$songId/$value"

        Log.d("FullURL", fullUrl)

        val mediaItem = MediaItem.Builder()
            .setUri(fullUrl)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        player.setMediaItem(mediaItem)
        player.setPlaybackSpeed(1.0f)
        player.prepare()
        player.playWhenReady = true
    }

    fun playAudioChunk(data: ByteArray, bytesRead: Int) {
        Log.d("AudioPlayer", "Play Audio Chunk")

        byteBuffer.addAll(data.toList())
        val byteArray = byteBuffer.toByteArray()
        val dataSourceFactory = DataSource.Factory {
            ByteArrayDataSource(byteArray)
        }
        val mediaItem = MediaItem.Builder().setUri("byteArrayUri").build() // URI fictif
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
        Log.d("AudioPlayer", "Player was ready and played (playWhenReady = true)")
    }

    // ------- Will run every second as long as the song is playing -------
    fun startListeningForPositionUpdate() {
        updateRunnable = Runnable {
            if (player.isPlaying) {
                songManager.timeListenedCurrentSong += 1
                //Log.d("ListenPosition", "Time listening: ${songManager.timeListenedCurrentSong}")

                if (songManager.timeListenedCurrentSong >= 10 && songManager.canSaveSongToListened) {
                    songManager.addCurrentSongToListened()
                    songManager.canSaveSongToListened = false
                }

                handler.postDelayed(updateRunnable!!, 1000)
            }
        }

        if (player.isPlaying) {
            handler.post(updateRunnable!!)
        }
    }

    fun stopListeningForPositionUpdate() {
        updateRunnable?.let {
            handler.removeCallbacks(it)
        }
    }
}