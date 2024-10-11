package com.ssw.kast.model.dto

class UnfollowDto {
    var unfollowerId: Any = ""
    var unfollowedId: Any = ""

    constructor()

    constructor(unfollowerId: Any, unfollowedId: Any) {
        this.unfollowerId = unfollowerId
        this.unfollowedId = unfollowedId
    }
}