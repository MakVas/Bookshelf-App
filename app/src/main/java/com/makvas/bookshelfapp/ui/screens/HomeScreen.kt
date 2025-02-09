package com.makvas.bookshelfapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
    bookState: BookState,
    retryAction: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
) {

    when (bookState) {
        is BookState.Success -> SuccessScreen(
            booksList = bookState.books,
            contentPadding = contentPadding,
            modifier = modifier.fillMaxSize()
        )

        BookState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())

        BookState.Error -> ErrorScreen(
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
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = contentPadding,
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
    ) {
        items(items = books, key = { book -> book.id }) { book ->
            BookItem(
                book = book,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth()
                    .height(350.dp)
            )
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(
                        book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
                    )
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.broken_image),
                placeholder = painterResource(R.drawable.loading_image),
                contentDescription = stringResource(R.string.book_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_small)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = book.volumeInfo.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                val authors = if (book.volumeInfo.authors.isNullOrEmpty())
                    listOf(stringResource(R.string.unknown_author))
                else book.volumeInfo.authors

                Text(
                    text = authors[0],
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
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
                        imageLinks = ImageLinks(
                            thumbnail = ""
                        ),
                        publisher = "",
                        publishedDate = ""
                    )
                )
            }
        )
        SuccessScreen(booksList = mockData)
    }
}