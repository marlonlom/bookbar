/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.features.book_detail

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
import androidx.compose.material.icons.twotone.BookmarkAdded
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
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.domain.books.BookDetailItem
import dev.marlonlom.apps.bookbar.ui.common.BookPosterImage
import dev.marlonlom.apps.bookbar.ui.main.BookbarAppState

/**
 * Raring bar for book details composable ui.
 *
 * @author marlonlom
 *
 * @param bookDetailItem Book detail item.
 */
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

/**
 * Book detail custom divider composable ui.
 *
 * @author marlonlom
 *
 */
@Composable
internal fun BookDetailDivider() {
  Divider(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp),
    color = MaterialTheme.colorScheme.secondary
  )
}

/**
 * Book details heading section composable ui.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param bookDetailItem Book detail item.
 * @param onBuyBookIconClicked Action for buy book icon button clicked.
 */
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

/**
 * Book detail heading inner content composable ui.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param bookDetailItem Book detail item.
 * @param onBuyBookIconClicked Action for buy book icon button clicked.
 */
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

/**
 * Buy book button composable ui.
 *
 * @author marlonlom
 *
 * @param bookDetailItem Book detail item.
 * @param onBuyBookIconClicked Action for buy book icon button clicked.
 */
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
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.onPrimary
    )
  ) {
    Image(
      Icons.TwoTone.ShoppingBag,
      contentDescription = stringResource(R.string.text_detail_buy),
      colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
      modifier = Modifier.size(20.dp)
    )
    Text(text = stringResource(R.string.text_detail_buy), Modifier.padding(start = 10.dp))
  }
}

/**
 * Book details header top bar composable ui.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param bookDetailItem Book detail item.
 * @param onBackNavigationIconClicked Action for back navigation button clicked.
 * @param onFavoriteBookIconClicked Action for favorite book toggle icon button clicked.
 * @param onShareBookIconClicked Action for share book icon button clicked.
 * @param backgroundColor Content background color.
 */
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
        appState.is7InTabletWidth.and(appState.isLandscapeOrientation.not())
      )
    if (showBackNavigationIcon) {
      BackNavigationIconButton(onBackNavigationIconClicked)
    }
    Spacer(modifier = Modifier.weight(1.0f))
    FavoriteBookIconButton(
      bookDetailItem, onFavoriteBookIconClicked
    )
    ShareBookIconButton(bookDetailItem, onShareBookIconClicked)
  }
}

/**
 * Book sharing button composable ui.
 *
 * @author marlonlom
 *
 * @param bookDetailItem Book detail item.
 * @param onShareBookIconClicked Action for sharing book button clicked.
 */
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

/**
 * Favorite book icon button composable ui.
 *
 * @author marlonlom
 *
 * @param onFavoriteBookIconClicked Action for favorite book toggle icon clicked.
 * @param bookDetailItem Book detail item.
 */
@Composable
internal fun FavoriteBookIconButton(
  bookDetailItem: BookDetailItem,
  onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit
) {
  IconButton(
    onClick = {
      onFavoriteBookIconClicked(bookDetailItem, bookDetailItem.favorite.not())
    },
  ) {
    val favoriteIconVector = when {
      bookDetailItem.favorite -> Icons.TwoTone.BookmarkAdded
      else -> Icons.TwoTone.BookmarkBorder
    }
    Icon(
      imageVector = favoriteIconVector,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.secondary,
      modifier = Modifier.size(24.dp)
    )
  }
}

/**
 * Back navigation button for book details composable ui.
 *
 * @author marlonlom
 *
 * @param onBackNavigationIconClicked Action for back navigation button clicked.
 */
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

/**
 * book detail text slot composable ui.
 *
 * @author marlonlom
 *
 * @param titleStringRes Title text as string resource.
 * @param sectionContent Section content as composable.
 */
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
