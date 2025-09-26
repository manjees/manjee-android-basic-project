package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.model.FavoriteBook
import com.manjee.basic.domain.repository.LikedBooksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteBooksUseCase @Inject constructor(
    private val likedBooksRepository: LikedBooksRepository
) {
    operator fun invoke(): Flow<List<FavoriteBook>> = likedBooksRepository.observeFavorites()
}
