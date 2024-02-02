/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.features.book_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.marlonlom.apps.bookbar.domain.books.BookDetailItem
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Book detail route composable ui.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param openExternalUrl Action for opening external urls.
 * @param onFavoriteBookIconClicked Action for favorite book button clicked.
 * @param onShareIconClicked Action for opening share dialog feature.
 */
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun BookDetailRoute(
    appState: BookbarAppState,
    openExternalUrl: (String) -> Unit,
    onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
    onShareIconClicked: (String) -> Unit,
) {
  BackHandler {
    appState.navController.popBackStack()
  }

  when (appState.bookDetails) {
    BookDetailResult.Loading -> {
      Text(text = "Loading book ...")
    }

    BookDetailResult.NotFound -> {
      Text(text = appState.bookDetails.toString())
    }

    is BookDetailResult.Success -> {
      BookDetailContent(
        appState = appState,
        bookDetailItem = appState.bookDetails.item,
        onBackNavigationIconClicked = {
          appState.navController.popBackStack()
        },
        onBuyBookIconClicked = openExternalUrl,
        onReadMoreTextClicked = openExternalUrl,
        onFavoriteBookIconClicked = onFavoriteBookIconClicked,
        onShareIconClicked = onShareIconClicked
      )
    }
  }

}
