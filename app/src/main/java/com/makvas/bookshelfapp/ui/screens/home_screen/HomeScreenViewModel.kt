package com.makvas.bookshelfapp.ui.screens.home_screen

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

    fun updateCurrentBook(book: Book) {
        _uiState.update {
            it.copy(selectedBook = book, isBookDetails = true)
        }
    }

    fun navigateToBookDetails() {
        _uiState.update {
            it.copy(isBookDetails = true)
        }
    }

    fun navigateToHomeScreen() {
        _uiState.update {
            it.copy(isBookDetails = false)
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun toggleSearch() {
        _uiState.update {
            it.copy(isSearch = !it.isSearch)
        }
    }

    fun getBooks() {
        _uiState.update { it.copy(isStartScreen = false) }
        viewModelScope.launch {
            _uiState.update { it.copy(bookResponse = BookResponse.Loading) }
            _uiState.update {
                try {
                    it.copy(
                        bookResponse = BookResponse.Success(
                            booksRepository.getBooks(
                                query = _uiState.value.searchQuery,
                                maxResults = 10
                            )
                        )
                    )
                } catch (e: IOException) {
                    it.copy(bookResponse = BookResponse.Error)
                } catch (e: HttpException) {
                    it.copy(bookResponse = BookResponse.Error)
                }
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