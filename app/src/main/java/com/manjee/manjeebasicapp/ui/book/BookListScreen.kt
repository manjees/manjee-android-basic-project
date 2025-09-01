package com.manjee.manjeebasicapp.ui.book

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun BookListScreen(viewModel: BookViewModel = hiltViewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val lazyPagingItems = viewModel.bookPagingFlow.collectAsLazyPagingItems()
    var text by remember { mutableStateOf(searchQuery) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                label = { Text("Search Books") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.setSearchQuery(text) }) {
                Text("Search")
            }
        }

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
                        Text(text = "Error: ${error.message}")
                        Button(onClick = { lazyPagingItems.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        count = lazyPagingItems.itemCount,
                        // Book 에 id 가 있으면 키/콘텐츠 타입 힌트도 추가 (선택)
                        // key = lazyPagingItems.itemKey { it.id },
                        // contentType = lazyPagingItems.itemContentType()
                    ) { index ->
                        val book = lazyPagingItems[index]
                        if (book != null) {
                            BookItem(book = book)
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
