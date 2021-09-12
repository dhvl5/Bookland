package com.dhaval.bookland.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.dhaval.bookland.models.Items
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.repositories.BookRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
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
                repository.getBooksList(it)
            )
            isLoading.value = false
        }
    }

    fun insertItem(item: Items) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    val getItems: LiveData<List<Items>> = repository.getItems().asLiveData()

    fun getItemsByStatus(status: Status): LiveData<List<Items>> {
        return repository.getItemsByStatus(status).asLiveData()
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            repository.deleteItemById(id)
        }
    }
}

class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookViewModel(repository) as T
    }
}