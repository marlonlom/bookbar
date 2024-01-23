/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.features.books_favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.ui.common.HeadlineTitle
import dev.marlonlom.apps.bookbar.ui.common.WelcomeTitle
import dev.marlonlom.apps.bookbar.ui.main.BookbarAppState

/**
 * Favorite books composable ui.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param onBookItemClicked Action for Book item clicked.
 * @param onRemoveFavoriteIconClicked Action for favorite book button clicked.
 */
@Composable
fun FavoriteBooksRoute(
  appState: BookbarAppState,
  onBookItemClicked: (String) -> Unit,
  onRemoveFavoriteIconClicked: (String) -> Unit,
) {
  val contentHorizontalPadding = when {
    appState.is10InTabletWidth -> 80.dp
    appState.is7InTabletWidth -> 40.dp
    else -> 20.dp
  }

  LazyVerticalGrid(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = contentHorizontalPadding)
      .background(MaterialTheme.colorScheme.background),
    columns = GridCells.Fixed(1),
    horizontalArrangement = Arrangement.spacedBy(20.dp),
    content = {
      item(span = { GridItemSpan(maxLineSpan) }) {
        WelcomeTitle(appState = appState)
      }

      item(span = { GridItemSpan(maxLineSpan) }) {
        HeadlineTitle(
          appState = appState,
          headlineTextRes = R.string.title_bottom_favorites
        )
      }

      when (appState.favoriteBooksList) {
        FavoriteBooksUiState.Empty -> {
          item {
            Text(text = "Empty results :(")
          }

        }

        FavoriteBooksUiState.Loading -> {
          item {
            Text(text = "Loading")
          }
        }

        is FavoriteBooksUiState.Success -> {
          val favoriteBooksCount = appState.favoriteBooksList.books.size
          items(favoriteBooksCount) { bookIndex ->
            ClickableFavoriteBookItem(
              bookIndex = bookIndex,
              bookItem = appState.favoriteBooksList.books[bookIndex],
              onBookItemClicked = onBookItemClicked,
              onRemoveFavoriteIconClicked = onRemoveFavoriteIconClicked
            )
            if ((bookIndex > 0).and(bookIndex < favoriteBooksCount)) {
              Divider(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 10.dp)
              )
            }
          }
        }

      }
    }
  )

}

