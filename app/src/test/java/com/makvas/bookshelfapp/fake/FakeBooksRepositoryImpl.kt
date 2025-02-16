package com.makvas.bookshelfapp.fake

import com.makvas.bookshelfapp.data.BooksRepository
import com.makvas.bookshelfapp.model.BookDetails
import com.makvas.bookshelfapp.model.BookList

class FakeBooksRepositoryImpl: BooksRepository {
    override suspend fun getBooks(query: String, maxResults: Int): BookList {
        return FakeDataSource.bookList
    }

    override suspend fun getBook(bookID: String): BookDetails {
        return FakeDataSource.bookDetails
    }
}