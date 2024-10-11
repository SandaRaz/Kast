package com.ssw.kast.model.dto

class AuthInfo {
    var identifier: String = ""
    var password: String = ""

    constructor()

    constructor(identifier: String, password: String) {
        this.identifier = identifier
        this.password = password
    }
}