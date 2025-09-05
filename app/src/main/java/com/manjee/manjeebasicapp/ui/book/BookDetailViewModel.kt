package com.manjee.manjeebasicapp.ui.book

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manjee.basic.domain.model.Book
import com.manjee.basic.domain.usecase.GetBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookUseCase: GetBookUseCase
) : ViewModel() {

    private val bookId: String = savedStateHandle.get<String>("bookId")!!

    private val _book = MutableStateFlow<Book?>(null)
    val book = _book.asStateFlow()

    init {
        viewModelScope.launch {
            _book.value = getBookUseCase(bookId)
        }
    }
}
