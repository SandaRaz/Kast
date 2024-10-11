package com.ssw.kast.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.dto.AuthInfo
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.network.repository.UserRepository
import com.ssw.kast.ui.screen.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _signedUser = mutableStateOf(User())
    private val signedUser: State<User> get() = _signedUser

    private suspend fun signIn(userCredential: AuthInfo): Boolean {
        this._signedUser.value = User.signIn(userRepository, userCredential)
        val validCredential = this.signedUser.value.errorCatcher.code == 0

        return validCredential
    }

    suspend fun onPressConnect(
        database: AppDatabase,
        navController: NavHostController,
        userLoginInput: String,
        passwordInput: String,
        credentialError: MutableState<String>,
        accountManager: AccountManager,
        songManager: SongManager,
        userDefaultPicture: ImageBitmap,
        userViewModel: UserViewModel,
        playlistDefaultCover: ImageBitmap,
        playlistViewModel: PlaylistViewModel
    ) {
        var inputError = 0
        if (userLoginInput.isBlank()) {
            inputError++
            credentialError.value = "Fill empty field"
        }
        if (passwordInput.isBlank()) {
            inputError++
            credentialError.value = "Fill empty field"
        }

        if (inputError == 0) {
            val credential = AuthInfo(userLoginInput, passwordInput)
            credentialError.value = ""

            val validCredential = this.signIn(credential)
            val signedUser = this.signedUser.value

            if (validCredential) {
                val userDao = User.getUserDao(signedUser)
                database.userDao().insert(userDao)

                val tableRow = database.userDao().countUsers()
                Log.d("SignInViewModel","User table row: $tableRow")

                signedUser.loadBitmapProfilePicture()
                accountManager.currentUser = signedUser

                userViewModel.refreshNewestUsers(
                    accountManager = accountManager,
                    navController = navController,
                    loggedUserId = signedUser.id,
                    amount = 5,
                    defaultProfilePicture = userDefaultPicture
                )

                songManager.refreshRecentSong(signedUser.id)
                playlistViewModel.refreshUserPlaylists(signedUser.id)
                playlistViewModel.refreshPlaylistCards(
                    navController = navController,
                    amount = 5,
                    defaultCover = playlistDefaultCover
                )
                playlistViewModel.refreshPlaylistPickers(signedUser.id)

                NavigationManager.navigateTo(navController, "home")
            } else {
                Log.e("SignInError", "Error: ${signedUser.errorCatcher.error}")
                credentialError.value = signedUser.errorCatcher.error
            }
        }
    }

    suspend fun signOut(accountManager: AccountManager, songManager: SongManager): Boolean {
        val success = User.signOut(accountManager.database, userRepository, accountManager)
        if (success) {
            songManager.listenedSongs.clear()
        }

        return success
    }
}