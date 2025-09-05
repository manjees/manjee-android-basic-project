package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.repository.BookRepository
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(id: String): Book? {
        return bookRepository.getBook(id)
    }
}
