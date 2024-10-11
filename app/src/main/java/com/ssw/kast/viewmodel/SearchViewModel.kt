package com.ssw.kast.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssw.kast.model.dto.SearchResultDto
import com.ssw.kast.network.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
    private val _searchResult = mutableStateOf(SearchResultDto())
    val searchResult: State<SearchResultDto> = _searchResult

    suspend fun search(userId: Any, keywords: String) {
        try {
            _searchResult.value = searchRepository.search(userId, keywords)
        } catch (e: Exception) {
            Log.e("Search", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }
}