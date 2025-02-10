package com.makvas.bookshelfapp.ui.screens.home_screen

import com.makvas.bookshelfapp.model.Book
import com.makvas.bookshelfapp.model.BookList

sealed interface BookResponse {
    data class Success(val books: BookList) : BookResponse
    data object Loading : BookResponse
    data object Error : BookResponse
}

data class HomeScreenUiState(
    val bookResponse: BookResponse = BookResponse.Loading,
    val searchQuery: String = "",
    val selectedBook: Book? = null,
    val isStartScreen: Boolean = true,
    val isSearch: Boolean = false,
    val isBookDetails: Boolean = false
)