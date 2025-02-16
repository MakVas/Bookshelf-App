package com.makvas.bookshelfapp.ui.screens.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.makvas.bookshelfapp.R
import com.makvas.bookshelfapp.model.Book
import com.makvas.bookshelfapp.model.BookList
import com.makvas.bookshelfapp.model.FakeDataSource
import com.makvas.bookshelfapp.ui.screens.aditional.ErrorScreen
import com.makvas.bookshelfapp.ui.screens.aditional.LoadingScreen
import com.makvas.bookshelfapp.ui.theme.BookshelfAppTheme
import com.makvas.bookshelfapp.ui.utils.ContentType
import com.makvas.bookshelfapp.ui.utils.Response

@Composable
fun HomeScreen(
    contentType: ContentType,
    bookResponse: Response,
    onBookPressed: (Book) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (bookResponse) {
        is Response.Success<*> -> SuccessHomeScreen(
            contentType = contentType,
            onBookPressed = onBookPressed,
            booksList = bookResponse.data as BookList,
            modifier = modifier.fillMaxSize()
        )

        Response.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())

        Response.Error -> ErrorScreen(
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
private fun SuccessHomeScreen(
    contentType: ContentType,
    onBookPressed: (Book) -> Unit,
    booksList: BookList,
    modifier: Modifier = Modifier,
) {
    val books = booksList.items

    if (contentType == ContentType.GRID) {
        BookGrid(
            books = books,
            onBookPressed = onBookPressed,
            modifier = modifier
        )
    } else {
        BookList(
            books = books,
            onBookPressed = onBookPressed,
            modifier = modifier
        )
    }

}

@Composable
private fun BookList(
    books: List<Book>,
    onBookPressed: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = books, key = { book -> book.id }) { book ->
            Column {
                BookItem(
                    book = book,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onBookPressed(book) }
                        .padding(
                            top = dimensionResource(R.dimen.padding_medium),
                            start = dimensionResource(R.dimen.padding_large),
                            end = dimensionResource(R.dimen.padding_large)
                        )
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.padding_medium)
                    ),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

@Composable
private fun BookGrid(
    books: List<Book>,
    onBookPressed: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(340.dp),
        contentPadding = PaddingValues(
            top = dimensionResource(R.dimen.padding_medium),
            start = dimensionResource(R.dimen.padding_small),
            end = dimensionResource(R.dimen.padding_small),
        ),
        modifier = modifier
    ) {
        items(items = books, key = { book -> book.id }) { book ->
            BookItem(
                book = book,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
                    .clickable { onBookPressed(book) }
            )
        }
    }
}

@Composable
private fun BookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(dimensionResource(R.dimen.image_height))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(
                    book.volumeInfo
                        .imageLinks?.thumbnail?.replace("http://", "https://")
                )
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.broken_image),
            placeholder = painterResource(R.drawable.loading_image),
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(dimensionResource(R.dimen.image_height))
                .width(dimensionResource(R.dimen.image_width))
        )
        Column(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_medium))
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = book.volumeInfo.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                val authors = if (book.volumeInfo.authors.isNullOrEmpty())
                    listOf(stringResource(R.string.unknown_author))
                else book.volumeInfo.authors

                Text(
                    text = authors[0],
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column {
                book.volumeInfo.categories?.let {
                    Text(
                        text = book.volumeInfo.categories.joinToString(separator = ", "),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    book.volumeInfo.publisher?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = book.volumeInfo.publishedDate?.take(4) ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListSuccessScreenPreview() {
    BookshelfAppTheme {
        SuccessHomeScreen(
            contentType = ContentType.LIST,
            booksList = FakeDataSource.bookList,
            onBookPressed = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GridSuccessScreenPreview() {
    BookshelfAppTheme {
        SuccessHomeScreen(
            contentType = ContentType.GRID,
            booksList = FakeDataSource.bookList,
            onBookPressed = {},
        )
    }
}