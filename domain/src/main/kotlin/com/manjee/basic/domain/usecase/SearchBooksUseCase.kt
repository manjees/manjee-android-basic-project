package com.manjee.basic.domain.usecase

import androidx.paging.PagingData
import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Book>> {
        return bookRepository.getBookStream(query)
    }
}
