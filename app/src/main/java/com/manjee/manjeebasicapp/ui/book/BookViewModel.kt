package com.manjee.manjeebasicapp.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.usecase.SearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("Android")
    val searchQuery = _searchQuery.asStateFlow()

    val bookPagingFlow: Flow<PagingData<Book>> = _searchQuery
        .flatMapLatest { query ->
            searchBooksUseCase(query)
        }
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
