package com.makvas.bookshelfapp.network

import com.makvas.bookshelfapp.model.BookDetails
import com.makvas.bookshelfapp.model.BookList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {

    @GET("books/v1/volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10,
    ): BookList

    @GET("books/v1/volumes/{bookID}")
    suspend fun getBook(
        @Path("bookID") bookID: String,
    ): BookDetails
}