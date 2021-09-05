package com.dhaval.bookland.repositories

import com.dhaval.bookland.models.Book
import com.dhaval.bookland.networking.RetrofitClient
import com.skydoves.sandwich.ApiResponse

object BookRepository {
    suspend fun getBooksList(query: String): ApiResponse<Book?> {
        return RetrofitClient.api.getBooks(query)
    }
}