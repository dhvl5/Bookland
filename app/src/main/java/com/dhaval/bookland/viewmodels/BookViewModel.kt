package com.dhaval.bookland.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.dhaval.bookland.models.Items
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.repositories.BookRepository
import com.dhaval.bookland.ui.components.main.BottomTab
import com.dhaval.bookland.utils.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    var emptySearchedResult: Boolean = false
    var showRemovingDialog: Boolean = false

    private val _bookQuery = MutableLiveData<String>()
    val isLoading = mutableStateOf(false)

    private val _selectedTab: MutableState<BottomTab> = mutableStateOf(BottomTab.TO_READ)
    val selectedTab: State<BottomTab> get() = _selectedTab

    fun selectTab(tab: BottomTab) {
        _selectedTab.value = tab
    }

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

    fun deleteItem(id: String, navController: NavHostController) {
        viewModelScope.launch {
            showRemovingDialog = true
            delay(500)
            repository.deleteItemById(id)
            showRemovingDialog = false
            navController.navigate(Screen.Main.route)
        }
    }
}

class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookViewModel(repository) as T
    }
}