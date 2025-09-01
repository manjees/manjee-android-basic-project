package com.manjee.basic.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.manjee.basic.data.local.database.AppDatabase
import com.manjee.basic.data.paging.BookRemoteMediator
import com.manjee.basic.data.remote.api.BookApi
import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookApi: BookApi,
    private val database: AppDatabase
) : BookRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getBookStream(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = BookRemoteMediator(
                query = query,
                database = database,
                bookApi = bookApi
            ),
            pagingSourceFactory = { database.bookDao().getBooks() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }
}
