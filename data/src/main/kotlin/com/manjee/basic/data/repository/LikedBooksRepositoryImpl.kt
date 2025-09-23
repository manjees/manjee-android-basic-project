package com.manjee.basic.data.repository

import com.manjee.basic.data.local.dao.FavoriteBookDao
import com.manjee.basic.data.local.entity.FavoriteBookEntity
import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.repository.LikedBooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LikedBooksRepositoryImpl @Inject constructor(
    private val favoriteBookDao: FavoriteBookDao
) : LikedBooksRepository {

    override fun observeLikedBookIds(): Flow<Set<String>> {
        return favoriteBookDao.observeFavoriteIds().map { it.toSet() }
    }

    override suspend fun toggle(book: Book) {
        if (favoriteBookDao.isFavorite(book.id)) {
            favoriteBookDao.delete(book.id)
        } else {
            favoriteBookDao.upsert(FavoriteBookEntity.from(book))
        }
    }
}
