/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.book_detail

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Bookmark
import androidx.compose.material.icons.twotone.BookmarkBorder
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material.icons.twotone.ShoppingBag
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.marlonlom.demos.bookbar.R
import dev.marlonlom.demos.bookbar.domain.books.BookDetailItem
import dev.marlonlom.demos.bookbar.ui.common.BookPosterImage
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState

@Composable
internal fun BookRatingsBar(bookDetailItem: BookDetailItem) {
  val bookRatingVal = bookDetailItem.rating.toInt()
  Row(modifier = Modifier.fillMaxWidth()) {
    for (i in 1..bookRatingVal)
      Icon(
        imageVector = Icons.TwoTone.Star,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.size(16.dp)
      )
  }
}

@Composable
internal fun BookDetailDivider() {
  Divider(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp),
    color = MaterialTheme.colorScheme.secondary
  )
}

@Composable
internal fun BookHeadingSection(
  appState: BookbarAppState,
  bookDetailItem: BookDetailItem,
  onBuyBookIconClicked: (String) -> Unit
) {
  val posterImageContainerWidth = if (appState.isLandscapeOrientation.and(appState.isCompactWidth.not())) 0.2f else 0.4f
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth(posterImageContainerWidth)) {
      BookPosterImage(
        bookTitle = bookDetailItem.title,
        bookPosterImage = bookDetailItem.image,
        imageHeight = 160.dp,
        aspectRatio = 3f / 4f
      )
    }
    Column(
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      BookHeadingContent(appState = appState, bookDetailItem, onBuyBookIconClicked)
    }
  }
}

@Composable
internal fun BookHeadingContent(
  appState: BookbarAppState,
  bookDetailItem: BookDetailItem,
  onBuyBookIconClicked: (String) -> Unit
) {
  BookRatingsBar(bookDetailItem)
  Text(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    text = bookDetailItem.title,
    fontWeight = FontWeight.Bold,
    style = MaterialTheme.typography.headlineSmall,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis,
  )
  Text(
    modifier = Modifier
      .fillMaxWidth()
      .paddingFromBaseline(top = 0.dp, bottom = 0.dp),
    text = bookDetailItem.subtitle,
    style = MaterialTheme.typography.bodySmall,
  )
  val priceBuyRowHorizontalArrangement = when {
    appState.isLandscapeOrientation.and(appState.isCompactHeight)
      .or(appState.is7InTabletWidth) -> Arrangement.spacedBy(30.dp)

    else -> Arrangement.SpaceBetween
  }
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = priceBuyRowHorizontalArrangement
  ) {
    Text(
      text = bookDetailItem.price,
      style = MaterialTheme.typography.bodyMedium,
    )
    BuyBookButton(bookDetailItem, onBuyBookIconClicked)
  }
}

@Composable
internal fun BuyBookButton(
  bookDetailItem: BookDetailItem,
  onBuyBookIconClicked: (String) -> Unit,
) {
  Button(
    onClick = {
      onBuyBookIconClicked(bookDetailItem.buyUrl)
    },
    colors = ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer,
      contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    )
  ) {
    Image(
      Icons.TwoTone.ShoppingBag,
      contentDescription = stringResource(R.string.text_detail_buy),
      colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
      modifier = Modifier.size(20.dp)
    )
    Text(text = stringResource(R.string.text_detail_buy), Modifier.padding(start = 10.dp))
  }
}

@Composable
internal fun HeaderTopBar(
  appState: BookbarAppState,
  bookDetailItem: BookDetailItem,
  onBackNavigationIconClicked: () -> Unit,
  onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
  onShareBookIconClicked: (String) -> Unit,
  backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(backgroundColor),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    val showBackNavigationIcon = appState.isCompactWidth
      .or(
        appState.isCompactHeight.and(appState.isLandscapeOrientation)
      ).or(
        appState.is7InTabletWidth.and(appState.isLandscapeOrientation.not())
      )
    if (showBackNavigationIcon) {
      BackNavigationIconButton(onBackNavigationIconClicked)
    }
    Spacer(modifier = Modifier.weight(1.0f))
    FavoriteBookIconButton(
      onFavoriteBookIconClicked, bookDetailItem
    )
    ShareBookIconButton(bookDetailItem, onShareBookIconClicked)
  }
}

@Composable
internal fun ShareBookIconButton(
  bookDetailItem: BookDetailItem,
  onShareBookIconClicked: (String) -> Unit
) {
  val shareMessage = stringResource(
    R.string.text_detail_book_sharing,
    bookDetailItem.title,
    bookDetailItem.url
  )
  IconButton(
    onClick = {
      onShareBookIconClicked(shareMessage)
    },
  ) {
    Icon(
      Icons.TwoTone.Share,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.secondary,
      modifier = Modifier.size(24.dp)
    )
  }
}

@Composable
internal fun FavoriteBookIconButton(
  onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
  bookDetailItem: BookDetailItem
) {
  IconButton(
    onClick = {
      onFavoriteBookIconClicked(bookDetailItem, bookDetailItem.favorite.not())
    },
  ) {
    val favoriteIconVector = if (bookDetailItem.favorite) Icons.TwoTone.Bookmark else Icons.TwoTone.BookmarkBorder
    Icon(
      imageVector = favoriteIconVector,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.secondary,
      modifier = Modifier.size(24.dp)
    )
  }
}

@Composable
internal fun BackNavigationIconButton(
  onBackNavigationIconClicked: () -> Unit,
) {
  IconButton(
    onClick = { onBackNavigationIconClicked() },
  ) {
    Icon(
      Icons.TwoTone.ArrowBack,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.secondary,
      modifier = Modifier.size(24.dp)
    )
  }
}

@Composable
internal fun BookTextSlot(
  @StringRes titleStringRes: Int,
  sectionContent: @Composable (() -> Unit),
) {
  Text(
    modifier = Modifier
      .fillMaxWidth()
      .paddingFromBaseline(top = 30.dp, bottom = 10.dp),
    text = stringResource(titleStringRes),
    fontWeight = FontWeight.Bold,
    style = MaterialTheme.typography.titleLarge,
    maxLines = 1
  )
  sectionContent()
}
