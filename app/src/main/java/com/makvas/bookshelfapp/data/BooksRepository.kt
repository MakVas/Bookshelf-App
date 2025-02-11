package com.makvas.bookshelfapp.data

import com.makvas.bookshelfapp.model.BookDetails
import com.makvas.bookshelfapp.model.BookList
import com.makvas.bookshelfapp.network.BooksApiService

interface BooksRepository {
    suspend fun getBooks(
        query: String,
        maxResults: Int
    ): BookList

    suspend fun getBook(
        bookID: String
    ): BookDetails
}

class BooksRepositoryImpl(
    private val booksApiService: BooksApiService
): BooksRepository {
    override suspend fun getBooks(
        query: String,
        maxResults: Int
    ): BookList = booksApiService.getBooks(
        query = query,
        maxResults = maxResults
    )

    override suspend fun getBook(
        bookID: String
    ): BookDetails = booksApiService.getBook(
        bookID = bookID
    )
}