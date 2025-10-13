package com.manjee.manjeebasicapp.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.manjee.basic.domain.model.Book
import com.manjee.manjeebasicapp.ui.theme.HeartIconBgDark
import com.manjee.manjeebasicapp.ui.theme.HeartIconLiked
import com.manjee.manjeebasicapp.ui.theme.RatingChipDark

import androidx.compose.foundation.clickable

@Composable
fun BookItem(
    book: Book,
    isLiked: Boolean,
    modifier: Modifier = Modifier,
    onToggleLike: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.thumbnail?.ifEmpty { "https://via.placeholder.com/120x160.png?text=No+Image" })
                .crossfade(true)
                .build(),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(120.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (book.authors.isNotEmpty()) {
                Text(
                    text = stringResource(
                        id = com.manjee.manjeebasicapp.R.string.book_author_by,
                        book.authors.joinToString(", ")
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            RatingChip(rating = 4.5) // API에 없으므로 임시 데이터
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = onToggleLike,
            modifier = Modifier.background(HeartIconBgDark, CircleShape)
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(id = com.manjee.manjeebasicapp.R.string.cd_toggle_favorite),
                tint = if (isLiked) HeartIconLiked else Color.White
            )
        }
    }
}

@Composable
fun RatingChip(rating: Double, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = RatingChipDark
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = rating.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
