package com.makvas.bookshelfapp.ui.utils

sealed interface Response {
    data class Success<T>(val data: T) : Response
    data object Loading : Response
    data object Error : Response
}

enum class ScreenType {
    START,
    HOME,
    DETAILS
}

enum class TopAppBarType {
    DEFAULT,
    SEARCH,
    DETAILS
}

enum class ContentType {
    LIST,
    GRID
}