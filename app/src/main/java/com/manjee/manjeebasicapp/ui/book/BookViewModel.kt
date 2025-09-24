package com.manjee.manjeebasicapp.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.usecase.ObserveLikedBookIdsUseCase
import com.manjee.basic.domain.usecase.SearchBooksUseCase
import com.manjee.basic.domain.usecase.ToggleLikedBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    observeLikedBookIdsUseCase: ObserveLikedBookIdsUseCase,
    private val toggleLikedBookUseCase: ToggleLikedBookUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("Android")
    val searchQuery = _searchQuery.asStateFlow()

    val bookPagingFlow: Flow<PagingData<Book>> = _searchQuery
        .flatMapLatest { query ->
            searchBooksUseCase(query)
        }
        .cachedIn(viewModelScope)

    val likedBookIds = observeLikedBookIdsUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleBookLike(book: Book) {
        viewModelScope.launch {
            toggleLikedBookUseCase(book)
        }
    }
}
