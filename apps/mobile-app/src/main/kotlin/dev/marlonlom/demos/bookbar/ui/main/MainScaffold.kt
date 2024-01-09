/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.main

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
import androidx.compose.runtime.State
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
import dev.marlonlom.demos.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.demos.bookbar.ui.features.books_favorite.FavoriteBooksUiState
import dev.marlonlom.demos.bookbar.ui.features.books_new.NewBooksUiState
import dev.marlonlom.demos.bookbar.ui.features.settings.UserSettingsDialog
import dev.marlonlom.demos.bookbar.ui.features.settings.UserSettingsViewModel
import dev.marlonlom.demos.bookbar.ui.navigation.BookbarRoutes
import dev.marlonlom.demos.bookbar.ui.navigation.MainBottomNavBar
import dev.marlonlom.demos.bookbar.ui.navigation.MainNavHost
import dev.marlonlom.demos.bookbar.ui.navigation.MainNavigationRail
import timber.log.Timber

/**
 * Main navigation host composable ui.
 *
 * @author marlonlom
 *
 * @param windowSizeClass Window size class.
 * @param userPreferencesRepository User preferences repository.
 * @param openOssLicencesInfo Action for opening oss licences information window.
 * @param openExternalUrl Action for opening oss licences information window.
 * @param newBooksListState New books list ui state.
 * @param favoriteBooksListState Favorite books list ui state.
 * @param appState Application ui state.
 */
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun MainScaffold(
  windowSizeClass: WindowSizeClass,
  userPreferencesRepository: UserPreferencesRepository,
  openOssLicencesInfo: () -> Unit,
  openExternalUrl: (String) -> Unit,
  newBooksListState: State<NewBooksUiState>,
  favoriteBooksListState: State<FavoriteBooksUiState>,
  appState: BookbarAppState = rememberBookbarAppState(
    windowSizeClass = windowSizeClass,
    newBooksList = newBooksListState.value,
    favoriteBooksList = favoriteBooksListState.value
  ),
) {
  var bottomNavSelectedIndex by remember { mutableIntStateOf(0) }
  var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

  val onBookItemClicked: (String) -> Unit = { bookIsbn13 ->
    Timber.d("Book[$bookIsbn13] Clicked.")
  }

  if (showSettingsDialog) {
    UserSettingsDialog(
      appState = appState,
      viewModel = UserSettingsViewModel(repository = userPreferencesRepository),
      onDialogDismissed = { showSettingsDialog = false },
      openOssLicencesInfo = openOssLicencesInfo,
      openExternalUrl = openExternalUrl
    )
  }

  Scaffold(
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    bottomBar = {
      if (appState.canShowBottomNavigation) {
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
      if (appState.canShowNavigationRail) {
        Row {
          MainNavigationRail(
            navSelectedIndex = bottomNavSelectedIndex,
            onNavSelectedIndexChanged = { selectedIndex, selectedRoute ->
              if (selectedRoute == BookbarRoutes.Preferences.route) {
                showSettingsDialog = true
              } else {
                bottomNavSelectedIndex = selectedIndex
                handleSelectedTopDestination(appState.navController, selectedRoute)
              }
            },
          )
          Column(modifier = Modifier.fillMaxWidth(0.4f)) {
            MainNavHost(appState, onBookItemClicked)
          }
          Column(
            modifier = Modifier
              .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RectangleShape
              )
              .padding(20.dp)
              .fillMaxSize()
          ) {
            Text(text = "Details?")
          }
        }
      } else {
        MainNavHost(appState, onBookItemClicked)
      }
    }
  }
}

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
