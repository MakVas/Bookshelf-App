package com.makvas.bookshelfapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.makvas.bookshelfapp.R
import com.makvas.bookshelfapp.model.Book
import com.makvas.bookshelfapp.ui.screens.aditional.BookshelfTopAppBar
import com.makvas.bookshelfapp.ui.screens.details_screen.DetailsScreen
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreen
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreenViewModel
import com.makvas.bookshelfapp.ui.screens.start_screen.StartScreen
import com.makvas.bookshelfapp.ui.utils.ContentType
import com.makvas.bookshelfapp.ui.utils.Response
import com.makvas.bookshelfapp.ui.utils.ScreenType
import com.makvas.bookshelfapp.ui.utils.TopAppBarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp(
    windowSize: WindowWidthSizeClass,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val contentType = when (windowSize) {
        WindowWidthSizeClass.Compact ->
            ContentType.LIST

        WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium ->
            ContentType.GRID

        else -> ContentType.LIST
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BookshelfTopAppBar(
                topAppBarType = uiState.topAppBarType,
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onSearchClick = viewModel::topAppBarTypeSearch,
                onBackClick = {
                    if (uiState.topAppBarType == TopAppBarType.DETAILS) {
                        viewModel.navigateToHomeScreen()
                        viewModel.topAppBarTypeSearch()
                    } else {
                        viewModel.topAppBarTypeDefault()
                    }
                },
                onSearch = {
                    viewModel.navigateToHomeScreen()
                    viewModel.getBooks()
                },
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) {
        HomeOrDetailsScreen(
            contentType = contentType,
            bookDetails = uiState.bookDetailsResponse,
            screenType = uiState.screenType,
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            onBookPressed = { book ->
                viewModel.getBook(book)
                viewModel.navigateToDetailsScreen()
                viewModel.topAppBarTypeDetails()
            },
            bookResponse = uiState.bookListResponse,
            retryAction = viewModel::getBooks,
            retryActionDetails = {
                viewModel.navigateToHomeScreen()
                viewModel.topAppBarTypeSearch()
            }
        )
    }
}

@Composable
private fun HomeOrDetailsScreen(
    contentType: ContentType,
    bookDetails: Response,
    screenType: ScreenType,
    onBookPressed: (Book) -> Unit,
    bookResponse: Response,
    retryAction: () -> Unit,
    retryActionDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.surface_corner_radius),
            topEnd = dimensionResource(id = R.dimen.surface_corner_radius),
        )
    ) {
        when (screenType) {
            ScreenType.DETAILS -> {
                DetailsScreen(
                    bookDetails = bookDetails,
                    retryAction = retryActionDetails,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            ScreenType.HOME -> {
                HomeScreen(
                    contentType = contentType,
                    onBookPressed = onBookPressed,
                    bookResponse = bookResponse,
                    retryAction = retryAction,
                )
            }

            else -> {
                StartScreen()
            }
        }
    }
}