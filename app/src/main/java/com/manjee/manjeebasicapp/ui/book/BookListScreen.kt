package com.manjee.manjeebasicapp.ui.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.manjee.manjeebasicapp.R

@Composable
fun BookListScreen(navController: NavController, viewModel: BookViewModel = hiltViewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val lazyPagingItems = viewModel.bookPagingFlow.collectAsLazyPagingItems()
    val likedBookIds by viewModel.likedBookIds.collectAsState()
    var text by remember { mutableStateOf(searchQuery) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.cd_search_books)
                    )
                },
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = com.manjee.manjeebasicapp.ui.theme.SurfaceDark,
                    unfocusedContainerColor = com.manjee.manjeebasicapp.ui.theme.SurfaceDark,
                    disabledContainerColor = com.manjee.manjeebasicapp.ui.theme.SurfaceDark,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.setSearchQuery(text)
                    keyboardController?.hide()
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val message = error.message ?: stringResource(id = R.string.error_unknown)
                            Text(text = stringResource(id = R.string.book_list_error, message))
                            Button(onClick = { lazyPagingItems.retry() }) {
                                Text(stringResource(id = R.string.action_retry))
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            // Book 에 id 가 있으면 키/콘텐츠 타입 힌트도 추가 (선택)
                            // key = lazyPagingItems.itemKey { it.id },
                            // contentType = lazyPagingItems.itemContentType()
                        ) { index ->
                            val book = lazyPagingItems[index]
                            if (book != null) {
                                val isLiked = likedBookIds.contains(book.id)
                                BookItem(
                                    book = book,
                                    isLiked = isLiked,
                                    onToggleLike = { viewModel.toggleBookLike(book) },
                                    onClick = {
                                        navController.navigate(com.manjee.manjeebasicapp.ui.navigation.Routes.getBookDetailPath(book.id))
                                    }
                                )
                            }
                        }

                        if (lazyPagingItems.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
