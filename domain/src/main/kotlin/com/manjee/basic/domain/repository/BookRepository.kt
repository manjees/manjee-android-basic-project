package com.manjee.basic.domain.repository

import androidx.paging.PagingData
import com.manjee.basic.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBookStream(query: String): Flow<PagingData<Book>>
}
