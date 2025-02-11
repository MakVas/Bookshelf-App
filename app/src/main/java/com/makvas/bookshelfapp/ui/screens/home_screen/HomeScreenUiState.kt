package com.makvas.bookshelfapp.ui.screens.home_screen

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

data class HomeScreenUiState(
    val bookListResponse: Response = Response.Loading,
    val bookDetailsResponse: Response = Response.Loading,
    val searchQuery: String = "",
    val screenType: ScreenType = ScreenType.START,
    val topAppBarType: TopAppBarType = TopAppBarType.DEFAULT
)