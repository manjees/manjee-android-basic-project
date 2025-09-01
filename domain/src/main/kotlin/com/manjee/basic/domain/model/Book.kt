
package com.manjee.basic.domain.model

data class Book(
    val id: String,
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
)
