package com.manjee.basic.data.remote.api

import com.manjee.basic.data.remote.model.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int
    ): BookSearchResponse

    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
    }
}
