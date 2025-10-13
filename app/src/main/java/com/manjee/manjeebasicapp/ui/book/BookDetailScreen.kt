package com.manjee.manjeebasicapp.ui.book

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import kotlin.math.abs
import com.manjee.manjeebasicapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val book by viewModel.book.collectAsState()

    var rotationY by remember { mutableStateOf(0f) }
    val animatedRotationY by animateFloatAsState(targetValue = rotationY, label = "rotationAnimation")

    Scaffold(
        topBar = {
                TopAppBar(
                    title = {
                        Text(text = book?.title ?: stringResource(id = R.string.book_loading))
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.cd_navigate_back)
                            )
                        }
                    }
                )
        }
    ) { innerPadding ->
        book?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            this.rotationY = animatedRotationY
                            cameraDistance = 12f * density
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    if (abs(dragAmount.x) > abs(dragAmount.y)) {
                                        change.consume()
                                        rotationY += dragAmount.x * 0.25f
                                    }
                                },
                                onDragEnd = { rotationY = 0f }
                            )
                        }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.thumbnail?.ifEmpty { "https://via.placeholder.com/240x320.png?text=No+Image" })
                            .crossfade(true)
                            .size(Size.ORIGINAL)
                            .build(),
                        contentDescription = it.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(240.dp)
                            .height(320.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (it.authors.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            id = R.string.book_author_by,
                            it.authors.joinToString(", ")
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
