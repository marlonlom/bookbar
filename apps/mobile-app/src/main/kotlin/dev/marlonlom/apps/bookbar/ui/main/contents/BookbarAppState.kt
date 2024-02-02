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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.FoldingFeature
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.ui.features.books_favorite.FavoriteBooksUiState
import dev.marlonlom.apps.bookbar.ui.features.books_new.NewBooksUiState
import dev.marlonlom.apps.bookbar.ui.util.DevicePosture

/**
 * Remembers the application ui state value.
 *
 * @author marlonlom
 *
 * @param windowSizeClass Window size class.
 * @param devicePosture Device posture.
 * @param newBooksList New books list value.
 * @param favoriteBooksList Favorite books list value.
 * @param detailedBook Book details value.
 * @param navController Navigation controller.
 * @param localConfiguration Local configuration.
 *
 * @return Application ui state value.
 */
@Composable
fun rememberBookbarAppState(
  windowSizeClass: WindowSizeClass,
  devicePosture: DevicePosture,
  newBooksList: NewBooksUiState,
  favoriteBooksList: FavoriteBooksUiState,
  detailedBook: BookDetailResult,
  navController: NavHostController = rememberNavController(),
  localConfiguration: Configuration = LocalConfiguration.current
): BookbarAppState = remember(
  windowSizeClass,
  navController,
  localConfiguration,
  devicePosture,
  newBooksList,
  detailedBook
) {
  BookbarAppState(
    navController = navController,
    windowSizeClass = windowSizeClass,
    localConfiguration = localConfiguration,
    devicePosture = devicePosture,
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
 * @param navController Navigation controller.
 * @param windowSizeClass Window size class.
 * @property devicePosture Device posture.
 * @param localConfiguration Local configuration.
 * @param newBooksList New books list value.
 * @param favoriteBooksList Favorite books list value.
 * @param bookDetails Book details value.
 */
@Stable
class BookbarAppState(
  internal val navController: NavHostController,
  private val windowSizeClass: WindowSizeClass,
  val devicePosture: DevicePosture,
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

  val canShowBottomNavigation get() = isCompactWidth

  val canShowNavigationRail get() = isCompactWidth.not().and(is10InTabletWidth.not())

  val canShowExpandedNavigationDrawer get() = isCompactWidth.not().and(is10InTabletWidth)

  val isDeviceBookPosture get() = devicePosture is DevicePosture.BookPosture

  val isDeviceBookPostureVertical
    get() = when (devicePosture) {
      is DevicePosture.BookPosture -> {
        devicePosture.orientation == FoldingFeature.Orientation.VERTICAL
      }

      else -> false
    }

  val isDeviceBookPostureHorizontal
    get() = when (devicePosture) {
      is DevicePosture.BookPosture -> {
        devicePosture.orientation == FoldingFeature.Orientation.HORIZONTAL
      }

      else -> false
    }

  val isDeviceSeparating get() = devicePosture is DevicePosture.Separating

  val isDeviceSeparatingVertical
    get() = when (devicePosture) {
      is DevicePosture.Separating -> {
        devicePosture.orientation == FoldingFeature.Orientation.VERTICAL
      }

      else -> false
    }

  val isDeviceSeparatingHorizontal
    get() = when (devicePosture) {
      is DevicePosture.Separating -> {
        devicePosture.orientation == FoldingFeature.Orientation.HORIZONTAL
      }

      else -> false
    }

  /**
   * Changes selected top destination.
   *
   * @param selectedRoute Selected destination route.
   */
  fun changeTopDestination(selectedRoute: String) {
    navController.navigate(selectedRoute) {
      popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
        inclusive = true
      }
      launchSingleTop = true
      restoreState = true
    }
  }
}
