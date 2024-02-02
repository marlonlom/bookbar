/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.marlonlom.apps.bookbar.domain.books.BookDetailItem
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailRoute
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailsViewModel
import dev.marlonlom.apps.bookbar.features.books_favorite.FavoriteBooksRoute
import dev.marlonlom.apps.bookbar.features.books_new.NewBooksRoute
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Main navigation host composable function.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 * @param bookDetailsViewModel Book details viewmodel.
 * @param onBookItemClicked Action for Book item clicked.
 * @param openExternalUrl Action for opening external urls.
 * @param onFavoriteBookIconClicked Action for favorite book icon clicked.
 * @param onRemoveFavoriteIconClicked Action for favorite Book removal icon clicked.
 * @param onShareIconClicked Action for book sharing icon clicked.
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
  onRemoveFavoriteIconClicked: (String) -> Unit,
  onShareIconClicked: (String) -> Unit
) {
  NavHost(
    navController = appState.navController,
    startDestination = BookbarRoute.Home.route
  ) {
    homeDestination(appState, onBookItemClicked)
    favoritesDestination(appState, onBookItemClicked, onRemoveFavoriteIconClicked)
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
@ExperimentalFoundationApi
internal fun NavGraphBuilder.homeDestination(
    appState: BookbarAppState,
    onBookItemClicked: (String) -> Unit
) {
  composable(BookbarRoute.Home.route) {
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
 * @param onRemoveFavoriteIconClicked Action for favorite Book removal icon clicked.
 */
internal fun NavGraphBuilder.favoritesDestination(
    appState: BookbarAppState,
    onBookItemClicked: (String) -> Unit,
    onRemoveFavoriteIconClicked: (String) -> Unit
) {
  composable(BookbarRoute.Favorite.route) {
    FavoriteBooksRoute(appState, onBookItemClicked, onRemoveFavoriteIconClicked)
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
    route = BookbarRoute.Details.route,
    arguments = BookbarRoute.Details.navArguments
  ) { navBackStackEntry ->
    val bookId = navBackStackEntry.arguments?.getString("bookId")
    requireNotNull(bookId) { "bookId parameter wasn't found. Please make sure it's set!" }
    bookDetailsViewModel.setSelectedBook(bookId)
    BookDetailRoute(appState, openExternalUrl, onFavoriteBookIconClicked, onShareIconClicked)
  }
}
