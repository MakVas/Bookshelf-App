package com.makvas.bookshelfapp.data

import com.makvas.bookshelfapp.model.BookList
import com.makvas.bookshelfapp.network.BooksApiService

interface BooksRepository {
    suspend fun getBooks(): BookList
}

class BooksRepositoryImpl(
    private val booksApiService: BooksApiService
): BooksRepository {
    override suspend fun getBooks(): BookList = booksApiService.getBooks()
}