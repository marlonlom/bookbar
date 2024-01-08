/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dev.marlonlom.demos.bookbar.BuildConfig
import dev.marlonlom.demos.bookbar.core.network.BookStoreApiServiceImpl
import dev.marlonlom.demos.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.demos.bookbar.dataStore
import dev.marlonlom.demos.bookbar.domain.books.BookstoreRepository
import dev.marlonlom.demos.bookbar.ui.features.books_new.NewBooksViewModel
import dev.marlonlom.demos.bookbar.ui.main.MainActivityUiState.Loading
import dev.marlonlom.demos.bookbar.ui.main.MainActivityUiState.Success
import dev.marlonlom.demos.bookbar.ui.theme.BookbarTheme
import dev.marlonlom.demos.bookbar.ui.util.CustomTabsOpener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Main activity class.
 *
 * @author marlonlom
 */
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
      AppContent(
        mainActivityUiState = mainActivityUiState,
        windowSizeClass = windowSizeClass,
        activityContext = this,
        userPreferencesRepository = newUserPreferencesRepository(),
        newBooksViewModel = newBooksViewModel
      )
    }
  }

  private fun newUserPreferencesRepository() = UserPreferencesRepository(dataStore)

  private fun newBookstoreRepository() = BookstoreRepository(
    BookStoreApiServiceImpl(BuildConfig.ITBOOKSTORE_API_URL)
  )

}

/**
 * Returns true/false if dynamic colors are applied to the ui.
 *
 * @param mainActivityUiState Main activity ui state.
 * @return true/false
 */
@Composable
private fun shouldUseDynamicColor(
  mainActivityUiState: MainActivityUiState
): Boolean = when (mainActivityUiState) {
  Loading -> false
  is Success -> mainActivityUiState.userData.useDynamicColor
}

/**
 * Returns true/false if dark theme is applied to the ui.
 *
 * @param mainActivityUiState Main activity ui state.
 * @return true/false
 */
@Composable
private fun shouldUseDarkTheme(
  mainActivityUiState: MainActivityUiState
): Boolean = when (mainActivityUiState) {
  Loading -> isSystemInDarkTheme()
  is Success -> {
    val useDarkTheme = mainActivityUiState.userData.useDarkTheme
    if (useDarkTheme.not()) isSystemInDarkTheme() else useDarkTheme
  }
}

/**
 * Application main content composable ui.
 *
 * @author marlonlom
 *
 * @param mainActivityUiState Main activity ui state.
 * @param windowSizeClass Window size class.
 * @param activityContext Activity context.
 * @param userPreferencesRepository User preferences repository.
 * @param newBooksViewModel New books list viewmodel.
 */
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
private fun AppContent(
  mainActivityUiState: MainActivityUiState,
  windowSizeClass: WindowSizeClass,
  activityContext: Context,
  userPreferencesRepository: UserPreferencesRepository,
  newBooksViewModel: NewBooksViewModel
) {
  BookbarTheme(
    darkTheme = shouldUseDarkTheme(mainActivityUiState),
    dynamicColor = shouldUseDynamicColor(mainActivityUiState)
  ) {

    val newBooksListState = newBooksViewModel.uiState.collectAsStateWithLifecycle()

    MainScaffold(
      windowSizeClass = windowSizeClass,
      userPreferencesRepository = userPreferencesRepository,
      openOssLicencesInfo = {
        Timber.d("[MainActivity.onCreate] Should open oss licences information content.")
        activityContext.startActivity(Intent(activityContext, OssLicensesMenuActivity::class.java))
      },
      openExternalUrl = { externalUrl ->
        Timber.d("[MainActivity.onCreate] Should open external url '$externalUrl'.")
        CustomTabsOpener.openUrl(activityContext, externalUrl)
      },
      newBooksListState = newBooksListState
    )
  }
}
