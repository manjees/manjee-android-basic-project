package com.manjee.basic.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.manjee.basic.data.local.database.AppDatabase
import com.manjee.basic.data.local.entity.BookEntity
import com.manjee.basic.data.local.entity.RemoteKeyEntity
import com.manjee.basic.data.remote.api.BookApi
import com.manjee.basic.data.remote.model.VolumeDto
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BookRemoteMediator(
    private val query: String,
    private val database: AppDatabase,
    private val bookApi: BookApi
) : RemoteMediator<Int, BookEntity>() {

    private val bookDao = database.bookDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, BookEntity>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(state.config.pageSize) ?: 0
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val response = bookApi.searchBooks(
                query = query,
                startIndex = loadKey,
                maxResults = state.config.pageSize
            )

            val books = response.items.orEmpty().mapNotNull { it.toEntity() }
            val endOfPaginationReached = books.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteByQuery(query)
                    bookDao.clearBooks()
                }
                val prevKey = if (loadKey == 0) null else loadKey - state.config.pageSize
                val nextKey = if (endOfPaginationReached) null else loadKey + state.config.pageSize
                val key = RemoteKeyEntity(query = query, prevKey = prevKey, nextKey = nextKey)
                remoteKeyDao.insertOrReplace(key)
                bookDao.insertAll(books)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun VolumeDto.toEntity(): BookEntity? {
        return volumeInfo?.let {
            BookEntity(
                id = id,
                title = it.title ?: "No Title",
                authors = it.authors ?: emptyList(),
                publisher = it.publisher,
                publishedDate = it.publishedDate,
                description = it.description,
                pageCount = it.pageCount,
                averageRating = it.averageRating,
                ratingsCount = it.ratingsCount,
                thumbnail = it.imageLinks?.thumbnail?.replace("http://", "https://"),
                infoLink = this.infoLink
            )
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, BookEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { remoteKeyDao.getRemoteKey(query) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, BookEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { remoteKeyDao.getRemoteKey(query) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, BookEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let { remoteKeyDao.getRemoteKey(query) }
        }
    }
}
