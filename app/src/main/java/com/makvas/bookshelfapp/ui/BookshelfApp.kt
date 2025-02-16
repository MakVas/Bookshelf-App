package com.makvas.bookshelfapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.makvas.bookshelfapp.R
import com.makvas.bookshelfapp.model.Book
import com.makvas.bookshelfapp.ui.screens.details_screen.DetailsScreen
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreen
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreenViewModel
import com.makvas.bookshelfapp.ui.screens.start_screen.StartScreen
import com.makvas.bookshelfapp.ui.utils.Response
import com.makvas.bookshelfapp.ui.utils.ScreenType
import com.makvas.bookshelfapp.ui.utils.TopAppBarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()

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
        MainOrDetailsScreen(
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
fun MainOrDetailsScreen(
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

@Composable
fun BookshelfTopAppBar(
    topAppBarType: TopAppBarType,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = topAppBarType
    ) { state ->
        when (state) {
            TopAppBarType.DEFAULT -> DefaultRow(onSearchClick = onSearchClick, modifier = modifier)

            TopAppBarType.SEARCH -> {
                SearchRow(
                    onBackClick = onBackClick,
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    onSearch = onSearch,
                    modifier = modifier
                )
            }

            TopAppBarType.DETAILS -> DetailsRow(onBackClick = onBackClick, modifier = modifier)
        }
    }
}

@Composable
fun DefaultRow(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_topappbar_top),
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_topappbar_bottom)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = onSearchClick) {
            Icon(Icons.Filled.Search, contentDescription = null)
        }
    }
}

@Composable
fun SearchRow(
    onBackClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                end = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(text = stringResource(R.string.search)) },
            singleLine = true,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.surface_corner_radius)),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DetailsRow(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_topappbar_top),
                bottom = dimensionResource(id = R.dimen.padding_topappbar_bottom),
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        Text(
            text = stringResource(R.string.book_details),
            style = MaterialTheme.typography.titleLarge
        )
    }
}