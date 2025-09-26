package com.manjee.basic.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.manjee.basic.data.local.database.AppDatabase
import com.manjee.basic.data.local.entity.FavoriteBookEntity
import com.manjee.basic.domain.model.Book
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteBookDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var favoriteBookDao: FavoriteBookDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        favoriteBookDao = database.favoriteBookDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertAndDeleteFavorite() = runBlocking {
        val book = sampleBook()
        favoriteBookDao.upsert(FavoriteBookEntity.from(book, likedAt = 1L))

        assertTrue(favoriteBookDao.isFavorite(book.id))
        assertTrue(favoriteBookDao.observeFavoriteIds().first().contains(book.id))

        favoriteBookDao.delete(book.id)
        assertFalse(favoriteBookDao.isFavorite(book.id))
        assertFalse(favoriteBookDao.observeFavoriteIds().first().contains(book.id))
    }

    @Test
    fun upsertReplacesExistingRecord() = runBlocking {
        val book = sampleBook()
        favoriteBookDao.upsert(FavoriteBookEntity.from(book, likedAt = 1L))
        val updated = book.copy(title = "New Title")
        favoriteBookDao.upsert(FavoriteBookEntity.from(updated, likedAt = 2L))

        val ids = favoriteBookDao.observeFavoriteIds().first()
        assertEquals(setOf(book.id), ids.toSet())
    }

    @Test
    fun observeFavoriteBooks_emitsInLikedAtOrder() = runBlocking {
        val first = sampleBook().copy(id = "fav-1", title = "A")
        val second = sampleBook().copy(id = "fav-2", title = "B")

        favoriteBookDao.upsert(FavoriteBookEntity.from(first, likedAt = 10L))
        favoriteBookDao.upsert(FavoriteBookEntity.from(second, likedAt = 20L))

        val favorites = favoriteBookDao.observeFavoriteBooks().first()
        assertEquals(listOf("fav-2", "fav-1"), favorites.map { it.id })
    }

    private fun sampleBook(): Book = Book(
        id = "fav-id",
        title = "Favorite",
        authors = listOf("Author"),
        publisher = null,
        publishedDate = null,
        description = null,
        pageCount = null,
        averageRating = null,
        ratingsCount = null,
        thumbnail = null,
        infoLink = null
    )
}
