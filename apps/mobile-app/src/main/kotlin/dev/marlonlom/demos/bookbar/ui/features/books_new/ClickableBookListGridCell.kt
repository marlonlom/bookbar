/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.books_new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.marlonlom.demos.bookbar.domain.books.BooksListDomainItem
import dev.marlonlom.demos.bookbar.ui.common.BookPosterImage
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState

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
  bookItem: BooksListDomainItem,
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
    BookPosterImage(bookItem.title, bookItem.image, imageHeight, 1f / 2f)
    BookTitleText(bookItem)
    BookPriceText(bookItem)
  }
}

@Composable
private fun BookPriceText(bookItem: BooksListDomainItem) {
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

@Composable
private fun BookTitleText(bookItem: BooksListDomainItem) {
  Text(
    modifier = Modifier
      .fillMaxWidth(0.8f)
      .padding(top = 0.dp),
    textAlign = TextAlign.Center,
    text = bookItem.title,
    style = MaterialTheme.typography.bodyMedium,
    fontWeight = FontWeight.SemiBold,
    color = MaterialTheme.colorScheme.secondary,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis,
  )
}
