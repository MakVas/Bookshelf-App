package com.makvas.bookshelfapp.ui.screens.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.makvas.bookshelfapp.BookshelfApplication
import com.makvas.bookshelfapp.data.BooksRepository
import com.makvas.bookshelfapp.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeScreenViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun navigateToHomeScreen() {
        _uiState.update {
            it.copy(screenType = ScreenType.HOME)
        }
    }

    fun navigateToDetailsScreen() {
        _uiState.update {
            it.copy(screenType = ScreenType.DETAILS)
        }
    }

    fun topAppBarTypeDefault() {
        _uiState.update {
            it.copy(topAppBarType = TopAppBarType.DEFAULT)
        }
    }

    fun topAppBarTypeSearch() {
        _uiState.update {
            it.copy(topAppBarType = TopAppBarType.SEARCH)
        }
    }

    fun topAppBarTypeDetails() {
        _uiState.update {
            it.copy(topAppBarType = TopAppBarType.DETAILS)
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun getBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(bookListResponse = Response.Loading) }
            try {
                val books = booksRepository.getBooks(
                    query = _uiState.value.searchQuery,
                    maxResults = 10
                )
                _uiState.update { it.copy(bookListResponse = Response.Success(books)) }
            } catch (e: IOException) {
                _uiState.update { it.copy(bookListResponse = Response.Error) }
            } catch (e: HttpException) {
                _uiState.update { it.copy(bookListResponse = Response.Error) }
            }
        }
    }


    fun getBook(book: Book) {
        viewModelScope.launch {
            _uiState.update { it.copy(bookDetailsResponse = Response.Loading) }
            try {
                val bookResponse = booksRepository.getBook(
                    bookID = book.id,
                )
                _uiState.update { it.copy(bookDetailsResponse = Response.Success(bookResponse)) }
                Log.d("HomeScreenViewModel", "getBook: $bookResponse")
            } catch (e: IOException) {
                _uiState.update { it.copy(bookDetailsResponse = Response.Error) }
                Log.d("HomeScreenViewModel", "getBook: $e")
            } catch (e: HttpException) {
                _uiState.update { it.copy(bookDetailsResponse = Response.Error) }
                Log.d("HomeScreenViewModel", "getBook: $e")
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