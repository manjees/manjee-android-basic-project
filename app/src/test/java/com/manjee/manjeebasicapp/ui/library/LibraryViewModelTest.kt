package com.manjee.manjeebasicapp.ui.library

import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.model.FavoriteBook
import com.manjee.basic.domain.repository.LikedBooksRepository
import com.manjee.basic.domain.usecase.ObserveFavoriteBooksUseCase
import com.manjee.basic.domain.usecase.RemoveFavoriteBookUseCase
import com.manjee.manjeebasicapp.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeLikedBooksRepository()
    private val observeFavorites = ObserveFavoriteBooksUseCase(repository)
    private val removeFavorite = RemoveFavoriteBookUseCase(repository)

    private fun createViewModel(): LibraryViewModel {
        return LibraryViewModel(observeFavorites, removeFavorite)
    }

    @Test
    fun `initial state sorts by recent likedAt`() = runTest {
        repository.setFavorites(
            listOf(
                FavoriteBook(sampleBook(id = "1", title = "B"), likedAt = 10L),
                FavoriteBook(sampleBook(id = "2", title = "A"), likedAt = 20L)
            )
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val ids = viewModel.uiState.value.favorites.map { it.book.id }
        assertEquals(listOf("2", "1"), ids)
    }

    @Test
    fun `selecting title sort reorders alphabetically`() = runTest {
        repository.setFavorites(
            listOf(
                FavoriteBook(sampleBook(id = "1", title = "Zulu"), likedAt = 20L),
                FavoriteBook(sampleBook(id = "2", title = "Alpha"), likedAt = 10L)
            )
        )

        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onSortSelected(FavoriteSort.TITLE)
        advanceUntilIdle()

        val titles = viewModel.uiState.value.favorites.map { it.book.title }
        assertEquals(listOf("Alpha", "Zulu"), titles)
    }

    @Test
    fun `removeFavorite updates state`() = runTest {
        repository.setFavorites(
            listOf(
                FavoriteBook(sampleBook(id = "1", title = "Keep"), likedAt = 10L),
                FavoriteBook(sampleBook(id = "2", title = "Drop"), likedAt = 20L)
            )
        )

        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.removeFavorite("2")
        advanceUntilIdle()

        val ids = viewModel.uiState.value.favorites.map { it.book.id }
        assertTrue(ids.contains("1"))
        assertEquals(1, ids.size)
    }

    private fun sampleBook(id: String, title: String) = Book(
        id = id,
        title = title,
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

    private class FakeLikedBooksRepository : LikedBooksRepository {
        private val favorites = MutableStateFlow<List<FavoriteBook>>(emptyList())

        fun setFavorites(newFavorites: List<FavoriteBook>) {
            favorites.value = newFavorites
        }

        override fun observeLikedBookIds(): Flow<Set<String>> = favorites.map { list ->
            list.map { it.book.id }.toSet()
        }

        override fun observeFavorites(): Flow<List<FavoriteBook>> = favorites

        override suspend fun toggle(book: Book) {
            favorites.value = if (favorites.value.any { it.book.id == book.id }) {
                favorites.value.filterNot { it.book.id == book.id }
            } else {
                favorites.value + FavoriteBook(book, likedAt = System.currentTimeMillis())
            }
        }

        override suspend fun remove(bookId: String) {
            favorites.value = favorites.value.filterNot { it.book.id == bookId }
        }
    }
}
