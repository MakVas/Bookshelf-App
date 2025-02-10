package com.makvas.bookshelfapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreen
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreenViewModel
import com.makvas.bookshelfapp.ui.screens.start_screen.StartScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Crossfade(
                targetState = uiState.isSearch,
            ) { target ->
                if (!target) {
                    BookshelfTopAppBar(
                        scrollBehavior = scrollBehavior,
                        onClick = viewModel::toggleSearch,
                    )
                } else {
                    SearchField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::updateSearchQuery,
                        onClick = viewModel::toggleSearch,
                        onSearch = viewModel::getBooks,
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                            .padding(
                                end = dimensionResource(R.dimen.padding_medium),
                                bottom = dimensionResource(R.dimen.padding_small),
                            )
                    )
                }
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = uiState.isStartScreen,
            ) { target ->
                if (target)
                    StartScreen()
                else {
                    HomeScreen(
                        bookResponse = uiState.bookResponse,
                        retryAction = viewModel::getBooks,
                        contentPadding = it
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            IconButton(
                onClick = onClick
            ) {
                Icon(Icons.Filled.Search, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
    )
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
        OutlinedTextField(
            placeholder = { Text(text = stringResource(R.string.search)) },
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(dimensionResource(R.dimen.surface_corner_radius)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

}