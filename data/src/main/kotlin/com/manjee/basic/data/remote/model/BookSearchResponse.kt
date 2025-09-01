package com.manjee.basic.data.remote.model

import com.google.gson.annotations.SerializedName

data class BookSearchResponse(
    @SerializedName("items") val items: List<VolumeDto>?
)

data class VolumeDto(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfoDto?,
    @SerializedName("infoLink") val infoLink: String?
)

data class VolumeInfoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("publishedDate") val publishedDate: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("pageCount") val pageCount: Int?,
    @SerializedName("averageRating") val averageRating: Double?,
    @SerializedName("ratingsCount") val ratingsCount: Int?,
    @SerializedName("imageLinks") val imageLinks: ImageLinksDto?
)

data class ImageLinksDto(
    @SerializedName("thumbnail") val thumbnail: String?
)
