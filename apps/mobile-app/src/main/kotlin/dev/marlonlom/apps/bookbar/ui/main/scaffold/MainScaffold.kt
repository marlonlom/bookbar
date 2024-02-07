/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.main.scaffold

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.marlonlom.apps.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailStatusContent
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailsViewModel
import dev.marlonlom.apps.bookbar.features.books_favorite.FavoriteBooksUiState
import dev.marlonlom.apps.bookbar.features.books_new.NewBooksUiState
import dev.marlonlom.apps.bookbar.features.settings.UserSettingsDialog
import dev.marlonlom.apps.bookbar.features.settings.UserSettingsViewModel
import dev.marlonlom.apps.bookbar.ui.main.contents.AppContentCallbacks
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState
import dev.marlonlom.apps.bookbar.ui.main.contents.rememberBookbarAppState
import dev.marlonlom.apps.bookbar.ui.main.scaffold.folding.FoldingSeparatingLandscapeContent
import dev.marlonlom.apps.bookbar.ui.main.scaffold.folding.FoldingSeparatingPortraitContent
import dev.marlonlom.apps.bookbar.ui.main.scaffold.folding.NotFoldedMediumContent
import dev.marlonlom.apps.bookbar.ui.main.scaffold.tablets.TabletScaffoldContent
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
 * @param devicePosture Device posture.
 * @param appContentCallbacks Application content callbacks.
 * @param userPreferencesRepository User preferences repository.
 * @param bookDetailsViewModel Book details view model.
 * @param newBooksListState New books list ui state.
 * @param favoriteBooksListState Favorite books list ui state.
 * @param detailedBookUiState Book details ui state.
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
  appContentCallbacks: AppContentCallbacks,
  userPreferencesRepository: UserPreferencesRepository,
  bookDetailsViewModel: BookDetailsViewModel,
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
  val currentAppRoute =
    appState.navController.currentBackStackEntryAsState().value?.destination?.route ?: BookbarRoute.Home.route

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
      Modifier.padding(paddingValues),
    ) {

      val listingUi: @Composable () -> Unit = {
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

      val detailsUi: @Composable () -> Unit = {
        BookDetailStatusContent(
          appState = appState,
          onBackNavigationIconClicked = {},
          onBuyBookIconClicked = appContentCallbacks.openExternalUrl,
          onReadMoreTextClicked = appContentCallbacks.openExternalUrl,
          onFavoriteBookIconClicked = appContentCallbacks.onFavoriteBookIconClicked,
          onShareIconClicked = appContentCallbacks.onShareIconClicked
        )
      }

      val navigationRailUi: @Composable (Int, (Int, String) -> Unit) -> Unit =
        { bottomNavIndex, bottomNavIndexChanged ->
          MainNavigationRail(
            navSelectedIndex = bottomNavIndex,
            onNavSelectedIndexChanged = bottomNavIndexChanged,
          )
        }

      when {
        appState.canShowExpandedNavigationDrawer.and(isTopDestination) -> {
          TabletScaffoldContent(
            appState = appState,
            selectedPosition = bottomNavSelectedIndex,
            onSelectedPositionChanged = { selectedIndex, selectedRoute ->
              if (selectedRoute == BookbarRoute.Settings.route) {
                showSettingsDialog = true
              } else {
                bottomNavSelectedIndex = selectedIndex
                appState.changeTopDestination(selectedRoute)
              }
            },
            firstContent = { listingUi() },
            secondContent = { detailsUi() }
          )
        }

        appState.canShowNavigationRail.and(isTopDestination).and(appState.isDeviceNormalPosture) -> {
          NotFoldedMediumContent(
            isLandscapeOrientation = appState.isLandscapeOrientation,
            navigationRail = {
              navigationRailUi(
                bottomNavSelectedIndex,
              ) { selectedIndex, selectedRoute ->
                if (selectedRoute == BookbarRoute.Settings.route) {
                  showSettingsDialog = true
                } else {
                  bottomNavSelectedIndex = selectedIndex
                  appState.changeTopDestination(selectedRoute)
                }
              }
            },
            leftContent = { listingUi() },
          ) { detailsUi() }
        }

        appState.canShowNavigationRail.and(isTopDestination).and(appState.isDeviceSeparatingHorizontal) -> {
          FoldingSeparatingLandscapeContent(
            navigationRail = {
              navigationRailUi(
                bottomNavSelectedIndex,
              ) { selectedIndex, selectedRoute ->
                if (selectedRoute == BookbarRoute.Settings.route) {
                  showSettingsDialog = true
                } else {
                  bottomNavSelectedIndex = selectedIndex
                  appState.changeTopDestination(selectedRoute)
                }
              }
            },
            leftContent = { listingUi() },
            rightContent = { detailsUi() },
          )
        }

        appState.canShowNavigationRail.and(isTopDestination).and(appState.isDeviceSeparatingVertical) -> {
          FoldingSeparatingPortraitContent(
            navigationRail = {
              navigationRailUi(
                bottomNavSelectedIndex,
              ) { selectedIndex, selectedRoute ->
                if (selectedRoute == BookbarRoute.Settings.route) {
                  showSettingsDialog = true
                } else {
                  bottomNavSelectedIndex = selectedIndex
                  appState.changeTopDestination(selectedRoute)
                }
              }
            },
            leftContent = { listingUi() },
            rightContent = { detailsUi() },
          )
        }

        else -> {
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
}
