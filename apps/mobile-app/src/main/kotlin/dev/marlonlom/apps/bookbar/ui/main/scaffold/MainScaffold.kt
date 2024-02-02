/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.main.scaffold

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.marlonlom.apps.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailContent
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailsViewModel
import dev.marlonlom.apps.bookbar.features.books_favorite.FavoriteBooksUiState
import dev.marlonlom.apps.bookbar.features.books_new.NewBooksUiState
import dev.marlonlom.apps.bookbar.features.settings.UserSettingsDialog
import dev.marlonlom.apps.bookbar.features.settings.UserSettingsViewModel
import dev.marlonlom.apps.bookbar.ui.main.contents.AppContentCallbacks
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState
import dev.marlonlom.apps.bookbar.ui.main.contents.rememberBookbarAppState
import dev.marlonlom.apps.bookbar.ui.navigation.BookbarRoute
import dev.marlonlom.apps.bookbar.ui.navigation.MainNavHost
import dev.marlonlom.apps.bookbar.ui.util.DevicePosture
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
  devicePosture: DevicePosture,
  userPreferencesRepository: UserPreferencesRepository,
  bookDetailsViewModel: BookDetailsViewModel,
  appContentCallbacks: AppContentCallbacks,
  newBooksListState: NewBooksUiState,
  favoriteBooksListState: FavoriteBooksUiState,
  detailedBookUiState: BookDetailResult,
  appState: BookbarAppState = rememberBookbarAppState(
    windowSizeClass = windowSizeClass,
    devicePosture = devicePosture,
    newBooksList = newBooksListState,
    favoriteBooksList = favoriteBooksListState,
    detailedBook = detailedBookUiState
  ),
) {
  val currentAppRoute = appState.navController
    .currentBackStackEntryAsState().value?.destination?.route
    ?: BookbarRoute.Home.route

  var bottomNavSelectedIndex by rememberSaveable {
    mutableIntStateOf(
      BookbarRoute.topDestinationRoutes.map { it.route }.indexOf(currentAppRoute)
    )
  }

  val isTopDestination = currentAppRoute in BookbarRoute.topDestinationRoutes.map { it.route }

  var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

  val onBookItemClicked: (String) -> Unit = { bookIsbn13 ->
    Timber.d("Book[$bookIsbn13] Clicked. canNavigateToDetail=${appState.canNavigateToDetail}")
    if (appState.canNavigateToDetail) {
      appState.navController.navigate(
        BookbarRoute.Details.createRoute(bookIsbn13)
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
            if (selectedRoute == BookbarRoute.Settings.route) {
              showSettingsDialog = true
            } else {
              bottomNavSelectedIndex = selectedIndex
              appState.changeTopDestination(selectedRoute)
            }
          },
        )
      }
    },
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .safeDrawingPadding()
        .navigationBarsPadding()
        .padding(paddingValues)
    ) {
      if (appState.canShowExpandedNavigationDrawer.and(isTopDestination)) {
        ExpandedNavigationDrawer(
          selectedPosition = bottomNavSelectedIndex,
          onSelectedPositionChanged = { selectedIndex, selectedRoute ->
            if (selectedRoute == BookbarRoute.Settings.route) {
              showSettingsDialog = true
            } else {
              bottomNavSelectedIndex = selectedIndex
              appState.changeTopDestination(selectedRoute)
            }
          },
        ) {
          if (appState.isLandscapeOrientation) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    shape = RoundedCornerShape(size = 20.dp)
                  )
                  .padding(horizontal = 20.dp)
                  .fillMaxSize(0.95f),
                horizontalAlignment = Alignment.CenterHorizontally
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
      } else if (appState.canShowNavigationRail.and(isTopDestination)) {
        MainScaffoldLandscapeContent(
          appState = appState,
          bottomNavSelectedIndex = bottomNavSelectedIndex,
          onNavSelectedIndexChanged = { selectedIndex, selectedRoute ->
            if (selectedRoute == BookbarRoute.Settings.route) {
              showSettingsDialog = true
            } else {
              bottomNavSelectedIndex = selectedIndex
              appState.changeTopDestination(selectedRoute)
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
  Row(verticalAlignment = Alignment.CenterVertically) {
    MainNavigationRail(
      navSelectedIndex = bottomNavSelectedIndex,
      onNavSelectedIndexChanged = onNavSelectedIndexChanged,
    )
    val fraction = if (appState.isLandscapeOrientation) 0.5f else 1f
    Column(modifier = Modifier.fillMaxWidth(fraction)) {
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
    if (appState.isLandscapeOrientation) {
      val detailContentBackgroundColor = MaterialTheme.colorScheme.secondaryContainer
      Column(
        modifier = Modifier
          .background(
            color = detailContentBackgroundColor,
            shape = RoundedCornerShape(size = 20.dp)
          )
          .padding(horizontal = 20.dp)
          .fillMaxSize(0.95f),
        horizontalAlignment = Alignment.CenterHorizontally
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
}
