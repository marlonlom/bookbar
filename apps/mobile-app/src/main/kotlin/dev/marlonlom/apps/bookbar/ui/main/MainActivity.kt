/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.WindowInfoTracker
import dev.marlonlom.apps.bookbar.BuildConfig
import dev.marlonlom.apps.bookbar.core.database.BookbarDatabase
import dev.marlonlom.apps.bookbar.core.network.BookStoreApiServiceImpl
import dev.marlonlom.apps.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.apps.bookbar.dataStore
import dev.marlonlom.apps.bookbar.domain.books.BookstoreRepository
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailsViewModel
import dev.marlonlom.apps.bookbar.features.books_favorite.FavoriteBooksViewModel
import dev.marlonlom.apps.bookbar.features.books_new.NewBooksViewModel
import dev.marlonlom.apps.bookbar.ui.main.MainActivityUiState.Loading
import dev.marlonlom.apps.bookbar.ui.main.MainActivityUiState.Success
import dev.marlonlom.apps.bookbar.ui.main.contents.AppContent
import dev.marlonlom.apps.bookbar.ui.util.DevicePosture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Main activity class.
 *
 * @author marlonlom
 */
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@ExperimentalMaterial3WindowSizeClassApi
class MainActivity : ComponentActivity() {

  private val mainActivityViewModel: MainActivityViewModel by viewModels(
    factoryProducer = {
      MainActivityViewModel.factory(newUserPreferencesRepository())
    })

  private val newBooksViewModel: NewBooksViewModel by viewModels(
    factoryProducer = {
      NewBooksViewModel.factory(newBookstoreRepository())
    })

  private val favoriteBooksViewModel: FavoriteBooksViewModel by viewModels(
    factoryProducer = {
      FavoriteBooksViewModel.factory(newBookstoreRepository())
    })

  private val bookDetailsViewModel: BookDetailsViewModel by viewModels(
    factoryProducer = {
      BookDetailsViewModel.factory(newBookstoreRepository())
    })

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val splashScreen = installSplashScreen()

    var mainActivityUiState: MainActivityUiState by mutableStateOf(Loading)

    lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        mainActivityViewModel.uiState
          .onEach { mainActivityUiState = it }
          .collect()
      }
    }

    splashScreen.setKeepOnScreenCondition {
      when (mainActivityUiState) {
        Loading -> true
        is Success -> false
      }
    }

    enableEdgeToEdge()

    setContent {
      val windowSizeClass = calculateWindowSizeClass(activity = this)
      val devicePosture by devicePostureFlow.collectAsStateWithLifecycle()

      AppContent(
        mainActivityUiState = mainActivityUiState,
        windowSizeClass = windowSizeClass,
        devicePosture = devicePosture,
        activityContext = this,
        userPreferencesRepository = newUserPreferencesRepository(),
        newBooksViewModel = newBooksViewModel,
        favoriteBooksViewModel = favoriteBooksViewModel,
        bookDetailsViewModel = bookDetailsViewModel
      )
    }
  }

  private fun newUserPreferencesRepository() = UserPreferencesRepository(dataStore)

  private fun newBookstoreRepository() = BookstoreRepository(
    bookstoreWebApi = BookStoreApiServiceImpl(baseUrl = BuildConfig.ITBOOKSTORE_API_URL),
    bookstoreDatabase = BookbarDatabase.getInstance(this)
  )

  private val devicePostureFlow = WindowInfoTracker
    .getOrCreate(this@MainActivity)
    .windowLayoutInfo(this@MainActivity)
    .flowWithLifecycle(lifecycle)
    .map { layoutInfo -> DevicePosture.fromLayoutInfo(layoutInfo) }
    .stateIn(
      scope = lifecycleScope,
      started = SharingStarted.Eagerly,
      initialValue = DevicePosture.NormalPosture
    )
}
