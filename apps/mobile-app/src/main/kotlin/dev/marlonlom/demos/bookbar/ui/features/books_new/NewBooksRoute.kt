/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.books_new

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.marlonlom.demos.bookbar.R
import dev.marlonlom.demos.bookbar.ui.common.HeadlineTitle
import dev.marlonlom.demos.bookbar.ui.common.WelcomeTitle
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState

/**
 * New books route composable ui.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param onBookItemClicked Action for Book item clicked.
 */
@Composable
fun NewBooksRoute(
  appState: BookbarAppState,
  onBookItemClicked: (String) -> Unit
) {
  val contentHorizontalPadding = when {
    appState.is10InTabletWidth -> 80.dp
    appState.is7InTabletWidth -> 40.dp
    else -> 20.dp
  }

  val gridColumnsCount = when {
    appState.is7InTabletWidth and appState.isLandscapeOrientation -> 2
    appState.is7InTabletWidth -> 3
    else -> 2
  }

  LazyVerticalGrid(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = contentHorizontalPadding)
      .background(MaterialTheme.colorScheme.background),
    columns = GridCells.Fixed(gridColumnsCount),
    horizontalArrangement = Arrangement.spacedBy(20.dp),
    content = {
      item(span = { GridItemSpan(maxLineSpan) }) {
        WelcomeTitle(appState = appState)
      }

      item(span = { GridItemSpan(maxLineSpan) }) {
        HeadlineTitle(
          appState = appState,
          headlineTextRes = R.string.title_bottom_new_releases
        )
      }

      when (appState.newBooksList) {
        NewBooksUiState.Empty -> {
          item {
            Text(text = "Empty results :(")
          }
        }

        NewBooksUiState.Loading -> {
          item {
            Text(text = "Loading")
          }
        }

        is NewBooksUiState.Success -> {
          items(appState.newBooksList.books.size) { bookIndex ->
            ClickableBookListGridCell(
              appState = appState,
              bookItem = appState.newBooksList.books[bookIndex],
              onBookItemClicked = onBookItemClicked
            )
          }
        }
      }

    }
  )
}
