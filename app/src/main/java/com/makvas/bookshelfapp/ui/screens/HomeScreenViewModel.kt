package com.makvas.bookshelfapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.makvas.bookshelfapp.BookshelfApplication
import com.makvas.bookshelfapp.data.BooksRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeScreenViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {
    var uiState: BookState by mutableStateOf(BookState.Loading)
        private set

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            uiState = BookState.Loading
            uiState = try {
                BookState.Success(booksRepository.getBooks())
            } catch (e: IOException) {
                BookState.Error
            } catch (e: HttpException) {
                BookState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksRepository = application.container.booksRepository
                HomeScreenViewModel(booksRepository = booksRepository)
            }
        }
    }
}