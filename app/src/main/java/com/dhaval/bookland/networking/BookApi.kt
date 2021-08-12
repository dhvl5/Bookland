package com.dhaval.bookland.networking

import Book
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun getBooks(@Query(value = "q") query: String?): Book
}