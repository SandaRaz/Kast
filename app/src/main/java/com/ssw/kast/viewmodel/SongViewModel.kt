package com.ssw.kast.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ssw.kast.model.component.ListCardModel
import com.ssw.kast.model.component.MusicCardModel
import com.ssw.kast.model.entity.MusicGenre
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.view.TopCategory
import com.ssw.kast.network.repository.SongRepository
import com.ssw.kast.ui.component.ListCard
import com.ssw.kast.ui.component.MusicCard
import com.ssw.kast.ui.screen.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {
    private val _mostStreamedSongs = mutableStateOf<List<Song>>(emptyList())
    val mostStreamedSongs = _mostStreamedSongs

    private val _mostStreamedCards = mutableStateOf<List<MusicCardModel>>(emptyList())
    val mostStreamedCards = _mostStreamedCards

    private val _listStreamedSongs = mutableStateOf<List<Song>>(emptyList())
    val listStreamedSongs: State<List<Song>> = _listStreamedSongs

    private val _musicGenreSongs = mutableStateOf<List<Song>>(emptyList())
    val musicGenreSongs = _musicGenreSongs

    private val _suggestions = mutableStateOf<List<Song>>(emptyList())
    val suggestions: State<List<Song>> = _suggestions

    private val _topCategories = mutableStateOf<List<TopCategory>>(emptyList())
    val topCategories = _topCategories

    private val _topCategoriesCards = mutableStateOf<List<MusicCardModel>>(emptyList())
    val topCategoriesCards = _topCategoriesCards

    private val _allCategoriesListCards = mutableStateOf<List<ListCardModel>>(emptyList())
    val allCategoriesListCards = _allCategoriesListCards

    fun loadMostStreamedSongs(
        navController: NavHostController,
        songManager: SongManager,
        amount: Int,
        defaultCover: ImageBitmap
    ) {
        viewModelScope.launch {
            if (_mostStreamedSongs.value.isEmpty()) {
                refreshMostStreamedSongs(
                    navController,
                    songManager,
                    amount,
                    defaultCover
                )
            }
        }
    }

    suspend fun refreshMostStreamedSongs(
        navController: NavHostController,
        songManager: SongManager,
        amount: Int,
        defaultCover: ImageBitmap
    ) {
        try {
            _mostStreamedSongs.value = songRepository.mostStreamedSongs(amount)

            val tempMostStreamedCards = mutableListOf<MusicCardModel>()
            mostStreamedSongs.value.forEach { song ->
                song.loadCoverBitmap()

                tempMostStreamedCards.add(
                    MusicCardModel(
                        title = song.title,
                        description = song.singer,
                        description2 = "${song.listeners} listener" + if (song.listeners > 1) "s" else "",
                        image = song.cover ?: defaultCover,
                        onClick = {
                            viewModelScope.launch {
                                try {
                                    songManager.onSongListChange(mostStreamedSongs.value)
                                    songManager.clickNewSong(song, true)
                                    NavigationManager.navigateTo(navController, "player")
                                } catch (e: Exception) {
                                    Log.e("MostStreamedSong", "Exception: ${e.message}")
                                    e.printStackTrace()
                                }
                            }
                        }
                    )
                )
            }

            _mostStreamedCards.value = tempMostStreamedCards
        } catch (e: Exception) {
            Log.e("Most Streamed", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadListStreamedSongs(amount: Int) {
        if (_listStreamedSongs.value.isEmpty()) {
            refreshListStreamedSongs(amount)
        }
    }

    suspend fun refreshListStreamedSongs(amount: Int) {
        try {
            _listStreamedSongs.value = songRepository.mostStreamedSongs(amount)
        } catch (e: Exception) {
            Log.e("List Streamed", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadMusicGenreSongs(
        userId: Any,
        musicGenreId: Any,
        offset: Int,
        limit: Int
    ) {
        try {
            _musicGenreSongs.value = songRepository.getMusicGenreSongs(
                userId,
                musicGenreId,
                offset,
                limit
            )
        } catch (e: Exception) {
            Log.e("SongViewModel_MusicGenreSongs", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadSuggestions(userId: Any) {
        viewModelScope.launch {
            if (_suggestions.value.isEmpty()) {
                //Log.d("LoadSuggestions", "Suggestions is still empty")
                refreshSuggestions(userId)
            }
        }
    }

    suspend fun refreshSuggestions(userId: Any) {
        try {
            _suggestions.value = songRepository.getSuggestedSongs(userId)
        } catch (e: Exception) {
            Log.e("SongViewModel_Refresh_Suggestion","Exception: ${e.message}")
        }
    }


    fun loadTopCategories(
        navController: NavHostController,
        songManager: SongManager,
        categoriesCover: ImageBitmap
    ) {
        viewModelScope.launch {
            if (_topCategories.value.isEmpty()) {
                refreshTopCategories(navController, songManager,categoriesCover)
            }
        }
    }

    suspend fun refreshTopCategories(
        navController: NavHostController,
        songManager: SongManager,
        categoriesCover: ImageBitmap
    ) {
        try {
            _topCategories.value = songRepository.getTopCategories()

            val tempTopCategoriesCard = mutableListOf<MusicCardModel>()
            topCategories.value.take(5).forEach { category ->
                tempTopCategoriesCard.add(
                    MusicCardModel(
                        title = category.musicGenreType,
                        description = "${category.value} song" + if (category.value > 1) "s" else "",
                        image = categoriesCover,
                        onClick = {
                            songManager.currentCategory = category
                            NavigationManager.navigateTo(navController,"category_music")
                            // Naviguer vers la liste de chanson de cet categorie
                        }
                    )
                )
            }

            _topCategoriesCards.value = tempTopCategoriesCard
        } catch (e: Exception) {
            Log.e("SongViewModel_Refresh_TopCategories", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun loadAllCategoriesCard(
        songManager: SongManager,
        navController: NavHostController,
        categoriesCover: ImageBitmap
    ) {
        if (_allCategoriesListCards.value.isEmpty()) {
            refreshAllCategoriesCard(songManager, navController, categoriesCover)
        }
    }

    suspend fun refreshAllCategoriesCard(
        songManager: SongManager,
        navController: NavHostController,
        categoriesCover: ImageBitmap
    ) {
        try {
            refreshTopCategories(navController, songManager, categoriesCover)

            val tempAllCategoriesCard = mutableListOf<ListCardModel>()
            topCategories.value.forEach { category ->
                tempAllCategoriesCard.add(
                    ListCardModel(
                        title = category.musicGenreType,
                        description = "${category.value} song" + if (category.value > 1) "s" else "",
                        image = categoriesCover,
                        onClick = {
                            songManager.currentCategory = category
                            NavigationManager.navigateTo(navController,"category_music")
                            // Naviguer vers la liste de chanson de cet categorie
                        }
                    )
                )
            }

            _allCategoriesListCards.value = tempAllCategoriesCard
        } catch (e: Exception) {
            Log.e("SongViewModel_Refresh_TopCategories", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }
}