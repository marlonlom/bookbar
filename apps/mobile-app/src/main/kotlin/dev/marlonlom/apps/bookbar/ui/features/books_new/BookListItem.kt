/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.features.books_new

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.domain.books.BooksListDomainItem
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState

/**
 * Clickable book grid cell for new books.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param bookItem Book list item.
 */
@Composable
internal fun BookListItem(
  appState: BookbarAppState,
  bookItem: BooksListDomainItem,
  onBookItemClicked: (String) -> Unit
) {

  val contentHeight = when {
    appState.is10InTabletWidth -> 160.dp
    else -> 120.dp
  }

  val imageWidth = when {
    appState.is10InTabletWidth -> 120.dp
    else -> 80.dp
  }

  Row(
    modifier = Modifier
      .background(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        shape = MaterialTheme.shapes.medium
      )
      .height(contentHeight)
      .padding(vertical = 4.dp)
      .fillMaxWidth()
      .clickable {
        onBookItemClicked(bookItem.isbn13)
      },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Column(
      modifier = Modifier
        .width(imageWidth)
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
          .aspectRatio(3f / 4f)
          .padding(4.dp)
      )
    }
    Column(modifier = Modifier.fillMaxWidth(0.9f)) {
      Text(
        textAlign = TextAlign.Start,
        text = bookItem.title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.secondary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        modifier = Modifier
          .padding(top = 4.dp),
        text = bookItem.price,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.secondary,
        maxLines = 1,
      )
    }
  }
}
