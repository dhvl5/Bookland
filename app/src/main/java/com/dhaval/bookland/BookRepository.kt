package com.dhaval.bookland

import Book
import com.dhaval.bookland.networking.RetrofitClient

object BookRepository {
    suspend fun getBooksList(query: String): Book {
        return RetrofitClient.api.getBooks(query)
    }
}