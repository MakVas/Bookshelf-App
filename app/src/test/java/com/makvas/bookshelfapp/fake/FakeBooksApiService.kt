package com.makvas.bookshelfapp.fake

import com.makvas.bookshelfapp.model.BookDetails
import com.makvas.bookshelfapp.model.BookList
import com.makvas.bookshelfapp.network.BooksApiService

class FakeBooksApiService: BooksApiService {
    override suspend fun getBooks(query: String, maxResults: Int): BookList {
        return FakeDataSource.bookList
    }

    override suspend fun getBook(bookID: String): BookDetails {
        return FakeDataSource.bookDetails
    }
}