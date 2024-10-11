package com.ssw.kast.model.entity

import org.threeten.bp.LocalDate

class UserActivity {
    lateinit var id: Any
    lateinit var preciseDate: LocalDate
    lateinit var activity: String
    lateinit var userId: Any
    lateinit var user: User
}