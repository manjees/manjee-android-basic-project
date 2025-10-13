package com.manjee.manjeebasicapp.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.annotation.StringRes
import com.manjee.basic.domain.model.FavoriteBook
import com.manjee.basic.domain.usecase.ObserveFavoriteBooksUseCase
import com.manjee.basic.domain.usecase.RemoveFavoriteBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    observeFavoriteBooksUseCase: ObserveFavoriteBooksUseCase,
    private val removeFavoriteBookUseCase: RemoveFavoriteBookUseCase
) : ViewModel() {

    private val sortOrder = MutableStateFlow(FavoriteSort.RECENT)

    private val favorites: StateFlow<List<FavoriteBook>> = observeFavoriteBooksUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val uiState: StateFlow<LibraryUiState> = combine(sortOrder, favorites) { sort, items ->
        val sorted = when (sort) {
            FavoriteSort.RECENT -> items.sortedByDescending { it.likedAt }
            FavoriteSort.TITLE -> items.sortedBy { it.book.title.lowercase(Locale.getDefault()) }
        }
        LibraryUiState(
            favorites = sorted,
            sort = sort
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LibraryUiState())

    fun onSortSelected(sort: FavoriteSort) {
        sortOrder.value = sort
    }

    fun removeFavorite(bookId: String) {
        viewModelScope.launch {
            removeFavoriteBookUseCase(bookId)
        }
    }
}

data class LibraryUiState(
    val favorites: List<FavoriteBook> = emptyList(),
    val sort: FavoriteSort = FavoriteSort.RECENT
)

enum class FavoriteSort(@StringRes val labelRes: Int) {
    RECENT(com.manjee.manjeebasicapp.R.string.library_sort_chip_recent),
    TITLE(com.manjee.manjeebasicapp.R.string.library_sort_chip_title)
}
