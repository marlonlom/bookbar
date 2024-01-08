/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.marlonlom.demos.bookbar.ui.features.books_favorite.FavoriteBooksRoute
import dev.marlonlom.demos.bookbar.ui.features.books_new.NewBooksRoute
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState

/**
 * Main navigation host composable function.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 */
@Composable
fun MainNavHost(
  appState: BookbarAppState
) {
  NavHost(
    navController = appState.navController,
    startDestination = BookbarRoutes.Home.route
  ) {
    homeDestination(appState)
    favoritesDestination(appState)
  }
}

/**
 * Home destination extension for navigation graph builder.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 */
internal fun NavGraphBuilder.homeDestination(
  appState: BookbarAppState
) {
  composable(BookbarRoutes.Home.route) {
    NewBooksRoute(appState)
  }
}

/**
 * Favorites destination extension for navigation graph builder.
 *
 * @author marlonlom
 *
 * @param appState App ui state.
 */
internal fun NavGraphBuilder.favoritesDestination(
  appState: BookbarAppState
) {
  composable(BookbarRoutes.Favorite.route) {
    FavoriteBooksRoute(appState)
  }
}
