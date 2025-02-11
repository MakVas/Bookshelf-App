package com.makvas.bookshelfapp.model

val defaultBook = Book(
    id = "1",
    volumeInfo = VolumeInfo(
        title = "Title",
        authors = listOf("Author"),
        categories = listOf("Category"),
        publisher = "Publisher",
        publishedDate = "Date",
        imageLinks = ImageLinks(
            thumbnail = ""
        )
    )
)

val defaultBookDetails = BookDetails(
    id = "1",
    volumeInfo = DetailVolumeInfo(
        title = "Title",
        authors = listOf("Author"),
        categories = listOf("Category"),
        publisher = "Publisher",
        publishedDate = "Date",
        imageLinks = ImageLinks(
            thumbnail = ""
        ),
        description = "Description",
        language = "Language",
        pageCount = 100
    )
)