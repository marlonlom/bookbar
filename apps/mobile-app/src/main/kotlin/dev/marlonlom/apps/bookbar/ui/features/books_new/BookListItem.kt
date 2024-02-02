/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.features.books_new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.domain.books.BooksListDomainItem
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState

@Composable
fun ClickableBookListItem(
) {
  val bookItem = BooksListDomainItem(
    isbn13 = "9781484292143",
    title = "Expert Performance Indexing in Azure SQL and SQL Server 2022, 4th Edition",
    price = "$58.79",
    image = "https://itbook.store/img/books/9781484292143.png"
  )
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp),
    verticalAlignment = Alignment.Bottom,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
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
        .width(80.dp)
        .height(120.dp)
        .aspectRatio(1f / 2f)
    )
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        modifier = Modifier
          .fillMaxWidth(),
        textAlign = TextAlign.Start,
        text = bookItem.title,
        style = MaterialTheme.typography.bodyLarge,
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
}

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

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable {
        onBookItemClicked(bookItem.isbn13)
      }
      .padding(horizontal = 20.dp),
    verticalAlignment = Alignment.Bottom,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
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
        .size(80.dp, 120.dp)
        .aspectRatio(1f / 2f)
    )
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        modifier = Modifier
          .fillMaxWidth(),
        textAlign = TextAlign.Start,
        text = bookItem.title,
        style = MaterialTheme.typography.bodyLarge,
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
}
