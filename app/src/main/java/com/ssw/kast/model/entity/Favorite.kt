package com.ssw.kast.model.entity

import org.threeten.bp.LocalDate

class Favorite {
    lateinit var id: Any
    lateinit var songId: Any
    lateinit var song: Song
    lateinit var userId: Any
    lateinit var user: User
    lateinit var addedAt: LocalDate
}