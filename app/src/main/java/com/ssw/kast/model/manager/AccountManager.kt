package com.ssw.kast.model.manager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.network.repository.UserRepository

class AccountManager {
    lateinit var database: AppDatabase
    private val _isStartup = mutableStateOf(true)
    var currentUser by mutableStateOf<User?>(null)
    var viewedUser by mutableStateOf<User?>(null)

    constructor()

    constructor(database: AppDatabase, currentUser: User?) {
        this.database = database
        this.currentUser = currentUser
    }

    constructor(database: AppDatabase, currentUser: User?, viewedUser: User?) {
        this.database = database
        this.currentUser = currentUser
        this.viewedUser = viewedUser
    }

    fun startupState(): MutableState<Boolean> {
        return this._isStartup
    }

    fun isOnStartup(): Boolean {
        return this._isStartup.value
    }

    fun switchOffStartupState() {
        this._isStartup.value = false
    }

    suspend fun loadLoggedUser(): Boolean {
        if (this.currentUser == null) {
            val userDao = this.database.userDao().getLatestUser()
            if (userDao == null) {
                return false
            } else {
                this.currentUser = User.getUser(userDao)
                this.currentUser!!.loadBitmapProfilePicture()

                return true
            }
        } else {
            return true
        }
    }

    suspend fun updateLoggedUser(
        userRepository: UserRepository
    ) {
        this.currentUser?.let { user ->
            this.currentUser = User.getUser(userRepository, user.id)

            val userDao = User.getUserDao(this.currentUser!!)
            this.database.userDao().updateUser(userDao)

            this.currentUser!!.loadBitmapProfilePicture()
        }
    }
}