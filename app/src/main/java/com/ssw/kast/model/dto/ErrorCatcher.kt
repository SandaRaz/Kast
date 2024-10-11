package com.ssw.kast.model.dto

import retrofit2.Response

class ErrorCatcher {
    var error: String = ""
    var code: Int = 0

    constructor()

    constructor(error: String, code: Int) {
        this.error = error
        this.code = code
    }

    companion object {
        fun getResponse(response: Response<ErrorCatcher>): ErrorCatcher {
            var errorCatcher = ErrorCatcher()
            if (response.isSuccessful) {
                errorCatcher = response.body()!!
            }
            return errorCatcher
        }
    }
}