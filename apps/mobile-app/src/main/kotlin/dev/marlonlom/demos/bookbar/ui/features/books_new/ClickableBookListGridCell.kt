/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.books_new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.marlonlom.demos.bookbar.R
import dev.marlonlom.demos.bookbar.domain.books.BooksListItem
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState
import timber.log.Timber

/**
 * Clickable book grid cell for new books.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param bookItem Book list item.
 */
@Composable
internal fun ClickableBookListGridCell(
  appState: BookbarAppState,
  bookItem: BooksListItem,
  onBookItemClicked: (String) -> Unit
) {

  val imageHeight = when {
    appState.is10InTabletWidth -> 220.dp
    appState.is7InTabletWidth -> 200.dp
    else -> 180.dp
  }

  Column(
    modifier = Modifier
      .clickable {
        onBookItemClicked(bookItem.isbn13)
      }
      .padding(vertical = 10.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(bookItem.image)
        .crossfade(true)
        .build(),
      placeholder = painterResource(R.drawable.img_books_placeholder),
      contentDescription = bookItem.title,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .height(imageHeight)
    )

    Text(
      modifier = Modifier
        .fillMaxWidth(0.8f)
        .padding(top = 10.dp),
      textAlign = TextAlign.Center,
      text = bookItem.title,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.secondary,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )

    Text(
      modifier = Modifier
        .padding(top = 4.dp),
      text = bookItem.price,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.Normal,
      color = MaterialTheme.colorScheme.secondary,
      maxLines = 1,
    )
  }
}
