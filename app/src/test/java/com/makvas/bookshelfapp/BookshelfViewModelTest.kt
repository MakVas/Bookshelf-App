package com.makvas.bookshelfapp

import com.makvas.bookshelfapp.fake.FakeBooksRepositoryImpl
import com.makvas.bookshelfapp.fake.FakeDataSource
import com.makvas.bookshelfapp.rules.TestDispatcherRule
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreenUiState
import com.makvas.bookshelfapp.ui.screens.home_screen.HomeScreenViewModel
import com.makvas.bookshelfapp.ui.utils.Response
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookshelfViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun homeScreenViewModel_getBooks_verifyBookListResponseSuccess() =
        runTest {
            val testViewModel = HomeScreenViewModel(
                booksRepository = FakeBooksRepositoryImpl()
            )
            testViewModel.getBooks()

            val uiState = HomeScreenUiState(
                bookListResponse = Response.Success(FakeDataSource.bookList)
            )

            assertEquals(
                uiState.bookListResponse,
                testViewModel.uiState.value.bookListResponse
            )
        }

    @Test
    fun homeScreenViewModel_getBookDetails_verifyBookDetailsResponseSuccess() =
        runTest {
            val testViewModel = HomeScreenViewModel(
                booksRepository = FakeBooksRepositoryImpl()
            )
            testViewModel.getBook(FakeDataSource.book)

            val uiState = HomeScreenUiState(
                bookDetailsResponse = Response.Success(FakeDataSource.bookDetails)
            )

            assertEquals(
                uiState.bookDetailsResponse,
                testViewModel.uiState.value.bookDetailsResponse
            )
        }
}