/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.marlonlom.demos.bookbar.domain.books.BookDetailItem
import dev.marlonlom.demos.bookbar.ui.features.book_detail.BookDetailRoute
import dev.marlonlom.demos.bookbar.ui.features.book_detail.BookDetailsViewModel
import dev.marlonlom.demos.bookbar.ui.features.books_favorite.FavoriteBooksRoute
import dev.marlonlom.demos.bookbar.ui.features.books_new.NewBooksRoute
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Main navigation host composable function.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param onBookItemClicked Action for Book item clicked.
 * @param openExternalUrl Action for opening external urls.
 */
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun MainNavHost(
  appState: BookbarAppState,
  bookDetailsViewModel: BookDetailsViewModel,
  onBookItemClicked: (String) -> Unit,
  openExternalUrl: (String) -> Unit,
  onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
  onShareIconClicked: (String) -> Unit
) {
  NavHost(
    navController = appState.navController,
    startDestination = BookbarRoutes.Home.route
  ) {
    homeDestination(appState, onBookItemClicked)
    favoritesDestination(appState, onBookItemClicked)
    detailsDestination(
      appState = appState,
      bookDetailsViewModel = bookDetailsViewModel,
      openExternalUrl = openExternalUrl,
      onFavoriteBookIconClicked = onFavoriteBookIconClicked,
      onShareIconClicked = onShareIconClicked
    )
  }
}

/**
 * Home destination extension for navigation graph builder.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param onBookItemClicked Action for Book item clicked.
 */
internal fun NavGraphBuilder.homeDestination(
  appState: BookbarAppState,
  onBookItemClicked: (String) -> Unit
) {
  composable(BookbarRoutes.Home.route) {
    NewBooksRoute(appState, onBookItemClicked)
  }
}

/**
 * Favorites destination extension for navigation graph builder.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param onBookItemClicked Action for Book item clicked.
 */
internal fun NavGraphBuilder.favoritesDestination(
  appState: BookbarAppState,
  onBookItemClicked: (String) -> Unit
) {
  composable(BookbarRoutes.Favorite.route) {
    FavoriteBooksRoute(appState, onBookItemClicked)
  }
}

/**
 * Book details destination extension for navigation graph builder.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param bookDetailsViewModel Book details viewmodel.
 * @param openExternalUrl Action for opening external urls.
 * @param onFavoriteBookIconClicked Action for favorite book button clicked.
 * @param onShareIconClicked Action for opening share dialog feature.
 */
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
internal fun NavGraphBuilder.detailsDestination(
  appState: BookbarAppState,
  bookDetailsViewModel: BookDetailsViewModel,
  openExternalUrl: (String) -> Unit,
  onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
  onShareIconClicked: (String) -> Unit
) {
  composable(
    route = BookbarRoutes.Details.route,
    arguments = BookbarRoutes.Details.navArguments
  ) { navBackStackEntry ->
    val bookId = navBackStackEntry.arguments?.getString("bookId")
    requireNotNull(bookId) { "bookId parameter wasn't found. Please make sure it's set!" }
    bookDetailsViewModel.setSelectedBook(bookId)
    BookDetailRoute(appState, openExternalUrl, onFavoriteBookIconClicked, onShareIconClicked)
  }
}
