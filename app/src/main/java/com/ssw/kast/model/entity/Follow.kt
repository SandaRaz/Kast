package com.ssw.kast.model.entity

import org.threeten.bp.LocalDate

class Follow {
    lateinit var id: Any
    lateinit var followerId: Any
    lateinit var follower: User
    lateinit var followedId: Any
    lateinit var followed: User
    var state: Int = 0
    lateinit var preciseDate: LocalDate
}