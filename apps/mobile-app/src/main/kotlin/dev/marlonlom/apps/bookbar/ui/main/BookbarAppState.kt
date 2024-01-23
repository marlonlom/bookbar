/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.main

import android.content.res.Configuration
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.ui.features.books_favorite.FavoriteBooksUiState
import dev.marlonlom.apps.bookbar.ui.features.books_new.NewBooksUiState

/**
 * Remembers the application ui state value.
 *
 * @author marlonlom
 *
 * @param windowSizeClass Window size class.
 * @param navController Navigation controller.
 *
 * @return Application ui state value.
 */
@Composable
fun rememberBookbarAppState(
  windowSizeClass: WindowSizeClass,
  navController: NavHostController = rememberNavController(),
  localConfiguration: Configuration = LocalConfiguration.current,
  newBooksList: NewBooksUiState,
  favoriteBooksList: FavoriteBooksUiState,
  detailedBook: BookDetailResult
): BookbarAppState = remember(
  windowSizeClass,
  navController,
  localConfiguration,
  newBooksList,
  detailedBook
) {
  BookbarAppState(
    navController = navController,
    windowSizeClass = windowSizeClass,
    localConfiguration = localConfiguration,
    newBooksList = newBooksList,
    favoriteBooksList = favoriteBooksList,
    bookDetails = detailedBook
  )
}

/**
 * Application ui state class.
 *
 * @author marlonlom
 *
 * @param windowSizeClass Window size class.
 * @param navController Navigation controller.
 * @param localConfiguration Local configuration.
 *
 */
@Stable
class BookbarAppState(
  internal val navController: NavHostController,
  private val windowSizeClass: WindowSizeClass,
  private val localConfiguration: Configuration,
  val newBooksList: NewBooksUiState,
  val favoriteBooksList: FavoriteBooksUiState,
  val bookDetails: BookDetailResult,
) {
  val canNavigateToDetail
    get() = isCompactWidth.or(isLandscapeOrientation.not())

  val isCompactWidth get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

  val isCompactHeight get() = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

  val isLandscapeOrientation get() = localConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE

  val is7InTabletWidth get() = localConfiguration.smallestScreenWidthDp.dp >= 600.dp

  val is10InTabletWidth get() = localConfiguration.smallestScreenWidthDp.dp >= 720.dp

  val canShowBottomNavigation get() = isCompactHeight.not().and(isLandscapeOrientation.not())

  val canShowNavigationRail get() = isCompactHeight.or(is7InTabletWidth.and(isLandscapeOrientation))
}
