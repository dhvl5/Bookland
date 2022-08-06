package com.dhaval.bookland.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.dhaval.bookland.models.Items
import com.dhaval.bookland.models.Status
import com.dhaval.bookland.repositories.BookRepository
import com.dhaval.bookland.ui.components.main.BottomTab
import com.dhaval.bookland.utils.Internet
import com.dhaval.bookland.utils.Screen
import com.skydoves.sandwich.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    var emptySearchedResult: Boolean = false
    var showRemovingDialog: Boolean = false

    val isLoading = mutableStateOf(false)

    val isNextItemsLoading = mutableStateOf(false)

    private val _selectedTab: MutableState<BottomTab> = mutableStateOf(BottomTab.TO_READ)
    val selectedTab: State<BottomTab> get() = _selectedTab

    private var _items = mutableStateListOf<Items>()
    val items : List<Items> = _items

    val errorType = mutableStateOf(Internet.ErrorType.NONE)

    fun selectTab(tab: BottomTab) {
        _selectedTab.value = tab
    }

    fun clearLoadItemsList() {
        _items.clear()
    }

    fun loadItems(query: String, startIndex: Int) {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000)
            val result = repository.getBooksList(query, startIndex)
            result.onSuccess {
                result.toLiveData {
                    if (data?.totalItems != 0) {
                        if(emptySearchedResult)
                            data?.items = emptyList()

                        data?.items?.let { _items.addAll(it) }
                        errorType.value = Internet.ErrorType.NONE
                    } else {
                        errorType.value = Internet.ErrorType.CUSTOM
                    }
                }
            }.onError {

            }.onException {
                if(!Internet.isAvailable()) {
                    errorType.value = Internet.ErrorType.INTERNET
                } else {
                    exception.message?.let {
                        errorType.value = Internet.ErrorType.EXCEPTION
                    }
                }
            }
            isLoading.value = false
        }
    }

    fun loadNextItems(query: String, startIndex: Int) {
        viewModelScope.launch {
            if (!isNextItemsLoading.value && (startIndex + 1) % 20 == 0) {
                isNextItemsLoading.value = true
                delay(1000)
                val result = repository.getBooksList(query, startIndex + 1)

                result.onSuccess {
                    this.data?.items?.let {
                        _items.addAll(it)
                    }
                }.onError {

                }.onException {
                    if(!Internet.isAvailable()) {
                        errorType.value = Internet.ErrorType.INTERNET
                    } else {
                        exception.message?.let {
                            errorType.value = Internet.ErrorType.EXCEPTION
                            errorType.value.errorMsg = it
                        }
                    }
                }
                isNextItemsLoading.value = false
            }
        }
    }

    fun insertItem(item: Items) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    val getItems: LiveData<List<Items>> = repository.getItems().asLiveData()

    fun clearDatabase() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

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
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(repository) as T
    }
}