/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.features.books_favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.BookmarkRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.marlonlom.apps.bookbar.domain.books.BooksListDomainItem
import dev.marlonlom.apps.bookbar.ui.common.BookPosterImage

@Composable
internal fun ClickableFavoriteBookItem(
  bookIndex: Int,
  bookItem: BooksListDomainItem,
  onBookItemClicked: (String) -> Unit,
  onRemoveFavoriteIconClicked: (String) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = if (bookIndex > 0) 10.dp else 0.dp)
      .clickable {
        onBookItemClicked(bookItem.isbn13)
      },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(0.25f),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      BookPosterImage(
        bookTitle = bookItem.title,
        bookPosterImage = bookItem.image,
        imageHeight = 140.dp,
        aspectRatio = 1f / 2f
      )
    }
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        IconButton(
          onClick = {
            onRemoveFavoriteIconClicked(bookItem.isbn13)
          },
        ) {
          Icon(
            imageVector = Icons.TwoTone.BookmarkRemove,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(24.dp)
          )
        }
      }
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = bookItem.title,
        style = MaterialTheme.typography.titleLarge,
        fontSize = MaterialTheme.typography.titleLarge.fontSize.value.minus(2.0f).sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold
      )
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = bookItem.price,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 1,
        fontStyle = FontStyle.Italic
      )
    }
  }
}
