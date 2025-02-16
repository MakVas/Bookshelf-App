package com.makvas.bookshelfapp.ui.screens.details_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.makvas.bookshelfapp.model.BookDetails
import com.makvas.bookshelfapp.model.FakeDataSource
import com.makvas.bookshelfapp.ui.screens.aditional.ErrorScreen
import com.makvas.bookshelfapp.ui.screens.aditional.LoadingScreen
import com.makvas.bookshelfapp.ui.theme.BookshelfAppTheme
import com.makvas.bookshelfapp.ui.utils.Response

@Composable
fun DetailsScreen(
    bookDetails: Response,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (bookDetails) {
        is Response.Success<*> -> {
            val bookData = bookDetails.data as BookDetails
            SuccessDetailsScreen(
                bookDetails = bookData,
                modifier = modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_medium),
                    end = dimensionResource(id = R.dimen.padding_medium),
                    top = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }

        Response.Loading -> LoadingScreen(modifier = modifier)

        Response.Error -> ErrorScreen(
            modifier = modifier,
            retryAction = retryAction
        )
    }
}

@Composable
private fun SuccessDetailsScreen(
    bookDetails: BookDetails,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dimensionResource(R.dimen.padding_medium))
                    .height(dimensionResource(R.dimen.image_detail_height)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = bookDetails.volumeInfo.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    bookDetails.volumeInfo.authors?.let {
                        Text(
                            text = it.joinToString(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Column {
                    bookDetails.volumeInfo.pageCount?.let {
                        DetailsRow(
                            title = stringResource(R.string.page_count),
                            content = it.toString()
                        )
                    }
                    bookDetails.volumeInfo.language?.let {
                        DetailsRow(
                            title = stringResource(R.string.language),
                            content = it
                        )
                    }
                    bookDetails.volumeInfo.publishedDate?.let {
                        DetailsRow(
                            title = stringResource(R.string.published_date),
                            content = it.take(4)
                        )
                    }
                    bookDetails.volumeInfo.publisher?.let {
                        DetailsRow(
                            title = stringResource(R.string.publisher),
                            content = it
                        )
                    }
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(
                        bookDetails.volumeInfo
                            .imageLinks?.thumbnail?.replace("http://", "https://")
                    )
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.broken_image),
                placeholder = painterResource(R.drawable.loading_image),
                contentDescription = stringResource(R.string.book_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.image_detail_height))
                    .width(dimensionResource(R.dimen.image_detail_width))
                    .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
            )
        }

        bookDetails.volumeInfo.categories?.let {
            Column {
                Text(
                    text = stringResource(R.string.category),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
                )
                Text(
                    text = it[0],
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        bookDetails.volumeInfo.description?.let {
            val originalString = it
            val cleanedString = originalString.replace("<p>", "").replace("</p>", "")
            Column {
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
                )
                Text(
                    text = cleanedString,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun DetailsRow(
    title: String,
    content: String,
) {
    val string = "$title: $content"
    Text(
        text = string,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Preview(showBackground = true)
@Composable
private fun SuccessDetailsScreenPreview() {
    BookshelfAppTheme {
        SuccessDetailsScreen(
            bookDetails = FakeDataSource.bookDetails
        )
    }
}
