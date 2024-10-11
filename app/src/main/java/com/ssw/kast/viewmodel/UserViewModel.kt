package com.ssw.kast.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ssw.kast.model.component.SmallCardModel
import com.ssw.kast.model.dto.ErrorCatcher
import com.ssw.kast.model.entity.Listen
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.network.repository.SongRepository
import com.ssw.kast.network.repository.UserRepository
import com.ssw.kast.ui.screen.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _followError = mutableStateOf(ErrorCatcher())
    val followError: State<ErrorCatcher> = _followError

    private val _listeningError = mutableStateOf(ErrorCatcher())
    val listeningError: State<ErrorCatcher> = _listeningError

    private val _newestUsers = mutableStateOf<List<User>>(emptyList())
    val newestUsers: State<List<User>> = _newestUsers

    private val _newestUserSmallCards = mutableStateOf<List<SmallCardModel>>(emptyList())
    val newestUserSmallCards: State<List<SmallCardModel>> = _newestUserSmallCards

    suspend fun follow(user: User, followedId: Any) {
        _followError.value = user.follow(userRepository, followedId)
    }

    suspend fun hasListenSong(listener: User, songId: Any, preciseTime: LocalDateTime) {
        _listeningError.value = listener.hasListenSong(userRepository, songId, preciseTime)
    }

    suspend fun saveListenedSongs(user: User, songManager: SongManager) {
        Log.d("ListenedSong", "Save all listened songs")
        try {
            songManager.listenedSongs.forEach {
                this.hasListenSong(user, it.songId, it.start)
            }
            songManager.listenedSongs.clear()
        } catch (e: Exception) {
            Log.e("ListenedSong", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun isFollowing(user: User, followedId: Any): Boolean {
        val isFollowing = user.isFollowing(userRepository, followedId)
        return isFollowing
    }

    fun loadNewestUsers(
        accountManager: AccountManager,
        navController: NavHostController,
        loggedUserId: Any,
        amount: Int,
        defaultProfilePicture: ImageBitmap
    ) {
        viewModelScope.launch {
            try {
                //Log.d("NewestUserSize","newestUsers size = ${newestUsers.value.size}")
                if (_newestUsers.value.isEmpty()) {
                    //Log.d("HomeViewModel", "NewestUsers list is still empty")
                    refreshNewestUsers(accountManager, navController, loggedUserId, amount, defaultProfilePicture)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel" ,"Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    suspend fun refreshNewestUsers(
        accountManager: AccountManager,
        navController: NavHostController,
        loggedUserId: Any,
        amount: Int,
        defaultProfilePicture: ImageBitmap
    ) {

        try {
            val nwUsers = userRepository.getNewestUsers(amount, loggedUserId)
            nwUsers.forEach { nwUser ->
                nwUser.loadBitmapProfilePicture()
            }
            _newestUsers.value = nwUsers

            val tempNewestUserSmallCards = mutableListOf<SmallCardModel>()
            _newestUsers.value.forEach { user ->
                tempNewestUserSmallCards.add(
                    SmallCardModel(
                        image = user.profilePicture ?: defaultProfilePicture,
                        primaryLabel = user.username,
                        secondaryLabel = "${user.followersCount} follower" + if (user.followersCount > 1) "s" else "",
                        onClick = {
                            try {
                                user.biography = "Hello, i am ${user.username} <3."
                                accountManager.viewedUser = user
                                NavigationManager.navigateTo(navController,"profile")
                            } catch (e: Exception) {
                                Log.e("Home Newest user", "Exception: ${e.message}")
                                e.printStackTrace()
                            }
                        }
                    )
                )
            }

            _newestUserSmallCards.value = tempNewestUserSmallCards
        } catch (e: Exception) {
            Log.e("HomeViewModel" ,"Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun unfollow(user: User, unfollowedId: Any) {
        _followError.value = user.unfollow(userRepository, unfollowedId)
    }

    suspend fun updateLoggedUser(
        accountManager: AccountManager
    ) {
        accountManager.updateLoggedUser(userRepository)
    }
}