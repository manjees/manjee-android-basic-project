package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.repository.LikedBooksRepository
import javax.inject.Inject

class RemoveFavoriteBookUseCase @Inject constructor(
    private val likedBooksRepository: LikedBooksRepository
) {
    suspend operator fun invoke(bookId: String) {
        likedBooksRepository.remove(bookId)
    }
}
