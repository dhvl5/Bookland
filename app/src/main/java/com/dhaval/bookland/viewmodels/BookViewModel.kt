package com.dhaval.bookland.viewmodels

import Book
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.dhaval.bookland.BookRepository
import com.dhaval.bookland.utils.Resource
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val _bookQuery = MutableLiveData<String>()

    fun addQuery(query: String) {
        _bookQuery.value = query
    }

    val bookQuery = _bookQuery.switchMap {
        liveData {
            emit(
                BookRepository.getBooksList(it)
            )
        }
    }
}