package com.ssw.kast.model.entity

import java.util.UUID

class Status {
    var id: Any = UUID.fromString("143150b4-0049-4282-932e-5c0b0409ced2")
    var label: String = ""
    var rank: Int = 1

    constructor()

    constructor(id: Any, label: String, rank: Int) {
        this.id = id
        this.label = label
        this.rank = rank
    }
}