package com.makvas.bookshelfapp.ui.screens

import com.makvas.bookshelfapp.model.BookList

sealed interface BookState {
    data class Success(val books: BookList): BookState
    data object Loading: BookState
    data object Error: BookState
}