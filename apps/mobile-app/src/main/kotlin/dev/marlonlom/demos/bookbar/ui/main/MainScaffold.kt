/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.marlonlom.demos.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.demos.bookbar.domain.books.BookDetailResult
import dev.marlonlom.demos.bookbar.ui.features.book_detail.BookDetailContent
import dev.marlonlom.demos.bookbar.ui.features.book_detail.BookDetailsViewModel
import dev.marlonlom.demos.bookbar.ui.features.books_favorite.FavoriteBooksUiState
import dev.marlonlom.demos.bookbar.ui.features.books_new.NewBooksUiState
import dev.marlonlom.demos.bookbar.ui.features.settings.UserSettingsDialog
import dev.marlonlom.demos.bookbar.ui.features.settings.UserSettingsViewModel
import dev.marlonlom.demos.bookbar.ui.navigation.BookbarRoutes
import dev.marlonlom.demos.bookbar.ui.navigation.MainBottomNavBar
import dev.marlonlom.demos.bookbar.ui.navigation.MainNavHost
import dev.marlonlom.demos.bookbar.ui.navigation.MainNavigationRail
import dev.marlonlom.demos.bookbar.ui.navigation.bottomNavRoutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * Main navigation host composable ui.
 *
 * @author marlonlom
 *
 * @param windowSizeClass Window size class.
 * @param userPreferencesRepository User preferences repository.
 * @param appContentCallbacks Application content callbacks.
 * @param newBooksListState New books list ui state.
 * @param favoriteBooksListState Favorite books list ui state.
 * @param appState Application ui state.
 */
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun MainScaffold(
  windowSizeClass: WindowSizeClass,
  userPreferencesRepository: UserPreferencesRepository,
  bookDetailsViewModel: BookDetailsViewModel,
  appContentCallbacks: AppContentCallbacks,
  newBooksListState: NewBooksUiState,
  favoriteBooksListState: FavoriteBooksUiState,
  detailedBookUiState: BookDetailResult,
  appState: BookbarAppState = rememberBookbarAppState(
    windowSizeClass = windowSizeClass,
    newBooksList = newBooksListState,
    favoriteBooksList = favoriteBooksListState,
    detailedBook = detailedBookUiState
  ),
) {
  val currentAppRoute = appState.navController
    .currentBackStackEntryAsState().value?.destination?.route ?: BookbarRoutes.Home.route
  var bottomNavSelectedIndex by remember {
    mutableIntStateOf(
      bottomNavRoutes.map { it.route }.indexOf(currentAppRoute)
    )
  }
  val isTopDestination = currentAppRoute in bottomNavRoutes.map { it.route }

  var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

  val onBookItemClicked: (String) -> Unit = { bookIsbn13 ->
    Timber.d("Book[$bookIsbn13] Clicked. canNavigateToDetail=${appState.canNavigateToDetail}")
    if (appState.canNavigateToDetail) {
      appState.navController.navigate(
        BookbarRoutes.Details.createRoute(bookIsbn13)
      )
    } else {
      bookDetailsViewModel.setSelectedBook(bookIsbn13)
    }
  }

  if (showSettingsDialog) {
    UserSettingsDialog(
      appState = appState,
      viewModel = UserSettingsViewModel(repository = userPreferencesRepository),
      onDialogDismissed = { showSettingsDialog = false },
      openOssLicencesInfo = appContentCallbacks.openOssLicencesInfo,
      openExternalUrl = appContentCallbacks.openExternalUrl
    )
  }

  Scaffold(
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    bottomBar = {
      if (appState.canShowBottomNavigation.and(isTopDestination)) {
        MainBottomNavBar(
          navSelectedIndex = bottomNavSelectedIndex,
          onNavSelectedIndexChanged = { selectedIndex, selectedRoute ->
            if (selectedRoute == BookbarRoutes.Preferences.route) {
              showSettingsDialog = true
            } else {
              bottomNavSelectedIndex = selectedIndex
              handleSelectedTopDestination(
                navController = appState.navController,
                selectedRoute = selectedRoute,
              )
            }
          },
        )
      }
    },
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .safeDrawingPadding()
        .padding(paddingValues)
    ) {
      if (appState.canShowNavigationRail.and(isTopDestination)) {
        MainScaffoldLandscapeContent(
          appState = appState,
          bottomNavSelectedIndex = bottomNavSelectedIndex,
          onNavSelectedIndexChanged = { selectedIndex, selectedRoute ->
            if (selectedRoute == BookbarRoutes.Preferences.route) {
              showSettingsDialog = true
            } else {
              bottomNavSelectedIndex = selectedIndex
              handleSelectedTopDestination(
                navController = appState.navController,
                selectedRoute = selectedRoute,
              )
            }
          },
          onBookItemClicked = onBookItemClicked,
          bookDetailsViewModel = bookDetailsViewModel,
          appContentCallbacks = appContentCallbacks,
        )
      } else {
        MainNavHost(
          appState = appState,
          bookDetailsViewModel = bookDetailsViewModel,
          onBookItemClicked = onBookItemClicked,
          openExternalUrl = appContentCallbacks.openExternalUrl,
          onFavoriteBookIconClicked = appContentCallbacks.onFavoriteBookIconClicked,
          onShareIconClicked = appContentCallbacks.onShareIconClicked,
          onRemoveFavoriteIconClicked = appContentCallbacks.onRemoveFavoriteIconClicked
        )
      }
    }
  }
}

/**
 * Main scaffold landscape content composable ui.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param bottomNavSelectedIndex Bottom navigation / Navigation rail selected index
 * @param onNavSelectedIndexChanged Action for changed top destination route.
 * @param onBookItemClicked Action for book list item clicked.
 * @param bookDetailsViewModel Book details viewmodel.
 * @param appContentCallbacks Application content callbacks.
 */
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
private fun MainScaffoldLandscapeContent(
  appState: BookbarAppState,
  bottomNavSelectedIndex: Int,
  onNavSelectedIndexChanged: (Int, String) -> Unit,
  onBookItemClicked: (String) -> Unit,
  bookDetailsViewModel: BookDetailsViewModel,
  appContentCallbacks: AppContentCallbacks,
) {
  Row {
    MainNavigationRail(
      navSelectedIndex = bottomNavSelectedIndex,
      onNavSelectedIndexChanged = onNavSelectedIndexChanged,
    )
    Column(modifier = Modifier.fillMaxWidth(0.4f)) {
      MainNavHost(
        appState = appState,
        bookDetailsViewModel = bookDetailsViewModel,
        onBookItemClicked = onBookItemClicked,
        openExternalUrl = appContentCallbacks.openExternalUrl,
        onFavoriteBookIconClicked = appContentCallbacks.onFavoriteBookIconClicked,
        onShareIconClicked = appContentCallbacks.onShareIconClicked,
        onRemoveFavoriteIconClicked = appContentCallbacks.onRemoveFavoriteIconClicked
      )
    }
    val detailContentBackgroundColor = MaterialTheme.colorScheme.secondaryContainer
    Column(
      modifier = Modifier
        .background(
          color = detailContentBackgroundColor,
          shape = RectangleShape
        )
        .padding(20.dp)
        .fillMaxSize()
    ) {
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
            onBackNavigationIconClicked = {},
            onBuyBookIconClicked = appContentCallbacks.openExternalUrl,
            onReadMoreTextClicked = appContentCallbacks.openExternalUrl,
            onFavoriteBookIconClicked = appContentCallbacks.onFavoriteBookIconClicked,
            onShareIconClicked = appContentCallbacks.onShareIconClicked,
            backgroundColor = detailContentBackgroundColor
          )
        }
      }
    }
  }
}

/**
 * Handle navigation to selected top destination.
 *
 * @param navController Navigation controller.
 * @param selectedRoute Selected destination route.
 */
internal fun handleSelectedTopDestination(
  navController: NavHostController,
  selectedRoute: String,
) {
  navController.navigate(selectedRoute) {
    popUpTo(navController.graph.findStartDestination().id) {
      saveState = true
      inclusive = true
    }
    launchSingleTop = true
    restoreState = true
  }
}
