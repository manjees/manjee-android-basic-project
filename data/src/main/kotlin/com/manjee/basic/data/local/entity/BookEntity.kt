package com.manjee.basic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manjee.basic.domain.model.Book

@Entity(tableName = "books")
data class BookEntity(
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
    val infoLink: String?
) {
    fun toDomainModel(): Book {
        return Book(
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
    }
}
