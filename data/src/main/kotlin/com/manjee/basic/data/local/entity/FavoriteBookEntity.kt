package com.manjee.basic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manjee.basic.domain.model.Book

@Entity(tableName = "favorite_books")
data class FavoriteBookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: List<String>,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val averageRating: Double?,
    val ratingsCount: Int?,
    val thumbnail: String?,
    val infoLink: String?,
    val likedAt: Long
) {
    fun toDomainModel(): Book = Book(
        id = id,
        title = title,
        authors = authors,
        publisher = publisher,
        publishedDate = publishedDate,
        description = description,
        pageCount = pageCount,
        averageRating = averageRating,
        ratingsCount = ratingsCount,
        thumbnail = thumbnail,
        infoLink = infoLink
    )

    companion object {
        fun from(book: Book, likedAt: Long): FavoriteBookEntity = FavoriteBookEntity(
            id = book.id,
            title = book.title,
            authors = book.authors,
            publisher = book.publisher,
            publishedDate = book.publishedDate,
            description = book.description,
            pageCount = book.pageCount,
            averageRating = book.averageRating,
            ratingsCount = book.ratingsCount,
            thumbnail = book.thumbnail,
            infoLink = book.infoLink,
            likedAt = likedAt
        )
    }
}
