package com.makvas.bookshelfapp.network

import com.makvas.bookshelfapp.model.BookList
import retrofit2.http.GET

interface BooksApiService {

    @GET("books/v1/volumes?q=jazz+history")
    suspend fun getBooks(): BookList
}