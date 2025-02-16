package com.makvas.bookshelfapp.model

object FakeDataSource {
    val book = Book(
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

    val bookList = BookList(
        items = List(10) {
            book.copy(id = it.toString())
        }
    )

    val bookDetails = BookDetails(
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
}
