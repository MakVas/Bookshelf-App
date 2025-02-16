package com.makvas.bookshelfapp

import com.makvas.bookshelfapp.data.BooksRepositoryImpl
import com.makvas.bookshelfapp.fake.FakeBooksApiService
import com.makvas.bookshelfapp.fake.FakeDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class BooksRepositoryImplTest {

    @Test
    fun networkBooksRepository_getBooks_verifyBookList() =
        runTest {
            val repository = BooksRepositoryImpl(
                booksApiService = FakeBooksApiService()
            )
            assertEquals(FakeDataSource.bookList, repository.getBooks("",10))
        }
}