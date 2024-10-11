package com.ssw.kast.model.entity

import androidx.compose.ui.graphics.ImageBitmap
import org.threeten.bp.LocalDate
import java.util.UUID

class Album {
    lateinit var id: Any
    lateinit var name: String
    lateinit var author: String
    lateinit var createdAt: LocalDate
    lateinit var creatorId: Any
    lateinit var creator: User
    lateinit var cover: ImageBitmap
    lateinit var coverCode: String


    constructor()

    constructor(id: Any, name: String) {
        this.id = id
        this.name = name
    }

    constructor(
        id: Any,
        name: String,
        author: String,
        createdAt: LocalDate,
        creatorId: Any,
        creator: User,
        cover: ImageBitmap,
        coverCode: String
    ) {
        this.id = id
        this.name = name
        this.author = author
        this.createdAt = createdAt
        this.creatorId = creatorId
        this.creator = creator
        this.cover = cover
        this.coverCode = coverCode
    }


}