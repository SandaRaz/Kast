package com.ssw.kast.model.entity

import androidx.compose.ui.graphics.ImageBitmap
import org.threeten.bp.LocalDate

class Album {
    lateinit var id: Any
    lateinit var name: String
    lateinit var author: String
    lateinit var createdAt: LocalDate
    lateinit var createdBy: User
    lateinit var cover: ImageBitmap
    lateinit var coverCode: String

    constructor(
        id: Any,
        name: String,
        author: String,
        createdAt: LocalDate,
        createdBy: User,
        cover: ImageBitmap,
        coverCode: String
    ) {
        this.id = id
        this.name = name
        this.author = author
        this.createdAt = createdAt
        this.createdBy = createdBy
        this.cover = cover
        this.coverCode = coverCode
    }

    constructor(
        name: String,
        author: String,
        createdAt: LocalDate,
        createdBy: User,
        cover: ImageBitmap,
        coverCode: String
    ) {
        this.name = name
        this.author = author
        this.createdAt = createdAt
        this.createdBy = createdBy
        this.cover = cover
        this.coverCode = coverCode
    }
}