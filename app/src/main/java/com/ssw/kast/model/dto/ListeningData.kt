package com.ssw.kast.model.dto

import com.ssw.kast.model.entity.User
import org.threeten.bp.LocalDateTime

class ListeningData {
    var listener: User? = null
    lateinit var songId: Any
    lateinit var start: LocalDateTime
    var end: LocalDateTime? = null

    constructor(listener: User?, songId: Any, start: LocalDateTime) {
        this.listener = listener
        this.songId = songId
        this.start = start
    }

    constructor(listener: User?, songId: Any, start: LocalDateTime, end: LocalDateTime?) {
        this.listener = listener
        this.songId = songId
        this.start = start
        this.end = end
    }

    constructor(songId: Any, start: LocalDateTime) {
        this.songId = songId
        this.start = start
    }


}