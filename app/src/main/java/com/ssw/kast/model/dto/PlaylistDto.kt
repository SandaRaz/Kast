package com.ssw.kast.model.dto

import org.threeten.bp.LocalDateTime

class PlaylistDto {
    var id: Any = ""
    var name: String = ""
    var createdAt: LocalDateTime = LocalDateTime.now()
    var userId: Any = ""

    constructor()
    constructor(id: Any, name: String, createdAt: LocalDateTime, userId: Any) {
        this.id = id
        this.name = name
        this.createdAt = createdAt
        this.userId = userId
    }
}