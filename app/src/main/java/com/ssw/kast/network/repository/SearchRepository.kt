package com.ssw.kast.network.repository

import com.ssw.kast.model.dto.SearchResultDto
import com.ssw.kast.network.service.SearchService
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchService: SearchService) {
    suspend fun search(userId: Any, keywords: String): SearchResultDto = searchService.search(userId, keywords)
}