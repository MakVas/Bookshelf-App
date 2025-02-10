package com.makvas.bookshelfapp.network

import com.makvas.bookshelfapp.model.BookList
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {

    @GET("books/v1/volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10,
    ): BookList
}