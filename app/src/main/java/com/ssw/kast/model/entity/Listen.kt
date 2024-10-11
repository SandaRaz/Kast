package com.ssw.kast.model.entity

import org.threeten.bp.LocalDateTime

class Listen {
    var id: Any = ""
    var songId: Any = ""
    var song: Song? = null
    var listenerId: Any = ""
    var listener: User? = null
    var preciseDate: LocalDateTime = LocalDateTime.now()

    constructor()

    constructor(id: Any, songId: Any, listenerId: Any, preciseDate: LocalDateTime) {
        this.id = id
        this.songId = songId
        this.listenerId = listenerId
        this.preciseDate = preciseDate
    }

    constructor(
        id: Any,
        songId: Any,
        song: Song?,
        listenerId: Any,
        listener: User?,
        preciseDate: LocalDateTime
    ) {
        this.id = id
        this.songId = songId
        this.song = song
        this.listenerId = listenerId
        this.listener = listener
        this.preciseDate = preciseDate
    }
}