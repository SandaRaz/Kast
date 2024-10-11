package com.ssw.kast.model.dto

class FollowDto {
    var followerId: Any = ""
    var followedId: Any = ""

    constructor()

    constructor(followerId: Any, followedId: Any) {
        this.followerId = followerId
        this.followedId = followedId
    }
}
