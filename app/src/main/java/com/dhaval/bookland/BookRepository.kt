package com.dhaval.bookland

import Book
import android.widget.Toast
import com.dhaval.bookland.networking.RetrofitClient
import com.dhaval.bookland.utils.Resource
import retrofit2.Response

object BookRepository {
    suspend fun getBooksList(query: String): Resource<Book?> {

        val response = try {
            RetrofitClient.api.getBooks(query)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }

        return Resource.Success(response)
    }
}