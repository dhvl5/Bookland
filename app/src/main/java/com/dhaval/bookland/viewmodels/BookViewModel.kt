package com.dhaval.bookland.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dhaval.bookland.BookRepository

class BookViewModel : ViewModel() {
    private val _bookQuery = MutableLiveData<String>()

    fun addQuery(query: String) {
        _bookQuery.value = query
    }

    val bookQuery = _bookQuery.switchMap {
        liveData {
            emit(BookRepository.getBooksList(it))
        }
    }
}