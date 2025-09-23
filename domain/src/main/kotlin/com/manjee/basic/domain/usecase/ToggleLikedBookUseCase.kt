package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.repository.LikedBooksRepository
import javax.inject.Inject

class ToggleLikedBookUseCase @Inject constructor(
    private val likedBooksRepository: LikedBooksRepository
) {
    suspend operator fun invoke(book: Book) {
        likedBooksRepository.toggle(book)
    }
}
