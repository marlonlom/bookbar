/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.features.books_new

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.ui.common.HeadlineTitle
import dev.marlonlom.apps.bookbar.ui.common.WelcomeTitle
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState

/**
 * New books route composable ui.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param onBookItemClicked Action for Book item clicked.
 */
@ExperimentalFoundationApi
@Composable
fun NewBooksRoute(
  appState: BookbarAppState,
  onBookItemClicked: (String) -> Unit
) {
  val contentHorizontalPadding = when {
    appState.is7InTabletWidth -> 40.dp
    else -> 20.dp
  }

  val verticalSpace = when {
    appState.is10InTabletWidth -> 20.dp
    else -> 10.dp
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .padding(contentHorizontalPadding),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(verticalSpace),
  ) {
    stickyHeader {
      Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        WelcomeTitle(appState = appState)
        HeadlineTitle(
          appState = appState,
          headlineTextRes = R.string.title_bottom_new_releases
        )
        Divider()
      }
    }

    when (appState.newBooksList) {
      NewBooksUiState.Empty -> {
        item {
          Text(text = "Empty results :(")
        }
      }

      NewBooksUiState.Loading -> {
        item {
          Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = "Loading", modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }

      is NewBooksUiState.Success -> {
        items(
          count = appState.newBooksList.books.size,
          key = { position ->
            appState.newBooksList.books[position].isbn13
          }
        ) { bookIndex ->
          BookListItem(
            appState = appState,
            bookItem = appState.newBooksList.books[bookIndex],
            onBookItemClicked = onBookItemClicked
          )
        }
      }
    }
  }

}
