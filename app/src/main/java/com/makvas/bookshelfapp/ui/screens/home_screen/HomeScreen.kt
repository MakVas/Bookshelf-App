package com.makvas.bookshelfapp.ui.screens.home_screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.makvas.bookshelfapp.R
import com.makvas.bookshelfapp.model.Book
import com.makvas.bookshelfapp.model.BookList
import com.makvas.bookshelfapp.model.ImageLinks
import com.makvas.bookshelfapp.model.VolumeInfo
import com.makvas.bookshelfapp.ui.theme.BookshelfAppTheme

@Composable
fun HomeScreen(
    bookResponse: BookResponse,
    retryAction: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
) {
    when (bookResponse) {
        is BookResponse.Success -> SuccessScreen(
            booksList = bookResponse.books,
            contentPadding = contentPadding,
            modifier = modifier.fillMaxSize()
        )

        BookResponse.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())

        BookResponse.Error -> ErrorScreen(
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
fun SuccessScreen(
    booksList: BookList,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val books = booksList.items

    Surface(
        modifier = modifier
            .padding(contentPadding)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(
            topStart = dimensionResource(R.dimen.surface_corner_radius),
            topEnd = dimensionResource(R.dimen.surface_corner_radius)
        )
    ) {

        LazyColumn {
            items(items = books, key = { book -> book.id }) { book ->
                BookItem(
                    book = book,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(top = dimensionResource(R.dimen.padding_medium))
                )
            }
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
        val mockData = BookList(
            items = List(10) {
                Book(
                    id = "$it",
                    volumeInfo = VolumeInfo(
                        title = "Book $it",
                        authors = listOf("Author"),
                        categories = listOf("Category"),
                        pageCount = 100,
                        publisher = "Publisher",
                        publishedDate = "2025",
                        imageLinks = ImageLinks(thumbnail = "")
                    )
                )
            }
        )
        SuccessScreen(booksList = mockData)
    }
}