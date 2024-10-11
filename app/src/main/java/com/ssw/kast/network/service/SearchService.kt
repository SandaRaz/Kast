package com.ssw.kast.network.service

import com.ssw.kast.model.dto.SearchResultDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchService {
    @GET("search/fuzzy/{userid}/{keywords}")
    suspend fun search(@Path("userid") userId: Any, @Path("keywords") keywords: String): SearchResultDto
}