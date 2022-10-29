package com.dhaval.bookland.repositories

import com.dhaval.bookland.db.BookDatabase
import com.dhaval.bookland.models.Book
import com.dhaval.bookland.models.Items
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.networking.BookApi
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookApi: BookApi, private val bookDatabase: BookDatabase) {
    suspend fun getBooksList(query: String, startIndex: Int): ApiResponse<Book?> {
        return bookApi.getBooks(
            query = query,
            startIndex = startIndex,
            maxResults = 20,
            apiKey = "AIzaSyAp4FNqMSThS2FWMpE2fKhJ0tKkTbYpbBc",
        )
    }

    suspend fun insertItem(item: Items) {
        bookDatabase.bookDao().insertItem(item)
    }

    fun getItems(): Flow<List<Items>> = bookDatabase.bookDao().getAllItems()

    suspend fun deleteAll() {
        bookDatabase.bookDao().deleteAll()
    }

    fun getItemsByStatus(status: Status): Flow<List<Items>> {
        return bookDatabase.bookDao().getAllItemsByStatus(status)
    }

    suspend fun deleteItemById(id: String) {
        bookDatabase.bookDao().deleteItemById(id)
    }
}