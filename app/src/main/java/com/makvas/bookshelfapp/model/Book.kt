package com.makvas.bookshelfapp.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class BookList (
    val items: List<Book>
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = emptyList(),
    val publisher: String? = null,
    val publishedDate: String? = null,
    val imageLinks: ImageLinks? = null
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class ImageLinks(
    val thumbnail: String
)