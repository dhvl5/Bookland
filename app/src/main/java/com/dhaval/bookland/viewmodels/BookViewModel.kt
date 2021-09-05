package com.dhaval.bookland.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.dhaval.bookland.repositories.BookRepository
import kotlinx.coroutines.delay

class BookViewModel : ViewModel() {
    private val _bookQuery = MutableLiveData<String>()
    val isLoading = mutableStateOf(false)

    fun addQuery(query: String) {
        _bookQuery.value = query
    }

    val bookQuery = _bookQuery.switchMap {
        liveData {
            isLoading.value = true
            delay(1000)
            emit(
                BookRepository.getBooksList(it)
            )
            isLoading.value = false
        }
    }
}