package com.manjee.basic.domain.repository

import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.model.FavoriteBook
import kotlinx.coroutines.flow.Flow

interface LikedBooksRepository {
    fun observeLikedBookIds(): Flow<Set<String>>
    fun observeFavorites(): Flow<List<FavoriteBook>>
    suspend fun toggle(book: Book)
    suspend fun remove(bookId: String)
}
