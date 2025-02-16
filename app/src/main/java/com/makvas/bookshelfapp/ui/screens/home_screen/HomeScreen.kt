package com.makvas.bookshelfapp.ui.screens.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.makvas.bookshelfapp.R
import com.makvas.bookshelfapp.model.Book
import com.makvas.bookshelfapp.model.BookList
import com.makvas.bookshelfapp.model.FakeDataSource
import com.makvas.bookshelfapp.ui.theme.BookshelfAppTheme
import com.makvas.bookshelfapp.ui.utils.Response

@Composable
fun HomeScreen(
    bookResponse: Response,
    onBookPressed: (Book) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (bookResponse) {
        is Response.Success<*> -> SuccessHomeScreen(
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
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.connection_error
            ),
            contentDescription = ""
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )

        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(
            text = stringResource(R.string.loading),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
fun SuccessHomeScreen(
    onBookPressed: (Book) -> Unit,
    booksList: BookList,
    modifier: Modifier = Modifier,
) {
    val books = booksList.items

    LazyColumn {
        items(items = books, key = { book -> book.id }) { book ->
            BookItem(
                book = book,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onBookPressed(book) }
                    .padding(top = dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .height(dimensionResource(R.dimen.image_height)),
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
        HorizontalDivider(
            modifier = Modifier.padding(
                top = dimensionResource(R.dimen.padding_medium)
            ),
            color = MaterialTheme.colorScheme.background
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    BookshelfAppTheme {
        ErrorScreen(retryAction = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    BookshelfAppTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    BookshelfAppTheme {
        SuccessHomeScreen(
            booksList = FakeDataSource.bookList,
            onBookPressed = {},
        )
    }
}