package com.manjee.manjeebasicapp.ui.book

import androidx.paging.PagingData
import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.repository.BookRepository
import com.manjee.basic.domain.model.FavoriteBook
import com.manjee.basic.domain.repository.LikedBooksRepository
import com.manjee.basic.domain.usecase.ObserveLikedBookIdsUseCase
import com.manjee.basic.domain.usecase.SearchBooksUseCase
import com.manjee.basic.domain.usecase.ToggleLikedBookUseCase
import com.manjee.manjeebasicapp.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeBookRepository = FakeBookRepository()
    private val fakeLikedRepository = FakeLikedBooksRepository()

    private val searchBooksUseCase = SearchBooksUseCase(fakeBookRepository)
    private val observeLikedBookIdsUseCase = ObserveLikedBookIdsUseCase(fakeLikedRepository)
    private val toggleLikedBookUseCase = ToggleLikedBookUseCase(fakeLikedRepository)

    private fun createViewModel(): BookViewModel {
        return BookViewModel(
            searchBooksUseCase = searchBooksUseCase,
            observeLikedBookIdsUseCase = observeLikedBookIdsUseCase,
            toggleLikedBookUseCase = toggleLikedBookUseCase
        )
    }

    @Test
    fun `toggleBookLike updates likedBookIds`() = runTest {
        val viewModel = createViewModel()
        val book = sampleBook()

        assertFalse(viewModel.likedBookIds.value.contains(book.id))

        viewModel.toggleBookLike(book)
        advanceUntilIdle()
        assertTrue(viewModel.likedBookIds.value.contains(book.id))

        viewModel.toggleBookLike(book)
        advanceUntilIdle()
        assertFalse(viewModel.likedBookIds.value.contains(book.id))
    }

    @Test
    fun `setSearchQuery updates StateFlow`() = runTest {
        val viewModel = createViewModel()
        val newQuery = "Compose"

        viewModel.setSearchQuery(newQuery)
        assertEquals(newQuery, viewModel.searchQuery.value)
    }

    private fun sampleBook() = Book(
        id = "id-1",
        title = "Test Title",
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

    private class FakeBookRepository : BookRepository {
        override fun getBookStream(query: String): Flow<PagingData<Book>> = flowOf(PagingData.empty())
        override suspend fun getBook(id: String): Book? = null
    }

    private class FakeLikedBooksRepository : LikedBooksRepository {
        private val favorites = MutableStateFlow<List<FavoriteBook>>(emptyList())
        private var counter = 0L

        override fun observeLikedBookIds(): Flow<Set<String>> = favorites.map { list ->
            list.map { it.book.id }.toSet()
        }

        override fun observeFavorites(): Flow<List<FavoriteBook>> = favorites

        override suspend fun toggle(book: Book) {
            if (favorites.value.any { it.book.id == book.id }) {
                remove(book.id)
            } else {
                counter += 1
                favorites.value = favorites.value + FavoriteBook(book, counter)
            }
        }

        override suspend fun remove(bookId: String) {
            favorites.value = favorites.value.filterNot { it.book.id == bookId }
        }
    }
}
