package com.makvas.bookshelfapp.ui.screens.home_screen

import com.makvas.bookshelfapp.ui.utils.Response
import com.makvas.bookshelfapp.ui.utils.ScreenType
import com.makvas.bookshelfapp.ui.utils.TopAppBarType

data class HomeScreenUiState(
    val bookListResponse: Response = Response.Loading,
    val bookDetailsResponse: Response = Response.Loading,
    val searchQuery: String = "",
    val screenType: ScreenType = ScreenType.START,
    val topAppBarType: TopAppBarType = TopAppBarType.DEFAULT
)