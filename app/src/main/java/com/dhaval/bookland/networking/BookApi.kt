package com.dhaval.bookland.networking

import com.dhaval.bookland.models.Book
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun getBooks(
        @Query(value = "q") query: String?,
        @Query(value = "startIndex") startIndex: Int,
        @Query(value = "maxResults") maxResults: Int,
        @Query(value = "apiKey") apiKey: String,
    ): ApiResponse<Book?>
}