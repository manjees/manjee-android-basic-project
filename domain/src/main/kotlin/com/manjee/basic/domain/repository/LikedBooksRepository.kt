package com.manjee.basic.domain.repository

import com.manjee.basic.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface LikedBooksRepository {
    fun observeLikedBookIds(): Flow<Set<String>>
    suspend fun toggle(book: Book)
}
