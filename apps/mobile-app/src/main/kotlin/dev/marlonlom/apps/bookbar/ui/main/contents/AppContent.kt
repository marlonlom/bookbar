package dev.marlonlom.apps.bookbar.ui.main.contents

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.marlonlom.apps.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.features.book_detail.BookDetailsViewModel
import dev.marlonlom.apps.bookbar.features.books_favorite.FavoriteBooksViewModel
import dev.marlonlom.apps.bookbar.features.books_new.NewBooksViewModel
import dev.marlonlom.apps.bookbar.ui.main.MainActivityUiState
import dev.marlonlom.apps.bookbar.ui.main.scaffold.MainScaffold
import dev.marlonlom.apps.bookbar.ui.theme.BookbarTheme
import dev.marlonlom.apps.bookbar.ui.util.DevicePosture
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
 * @param bookDetailsViewModel Book details viewmodel.
 */
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun AppContent(
  mainActivityUiState: MainActivityUiState,
  windowSizeClass: WindowSizeClass,
  devicePosture: DevicePosture,
  activityContext: Context,
  userPreferencesRepository: UserPreferencesRepository,
  newBooksViewModel: NewBooksViewModel,
  favoriteBooksViewModel: FavoriteBooksViewModel,
  bookDetailsViewModel: BookDetailsViewModel
) {
  BookbarTheme(
    darkTheme = shouldUseDarkTheme(mainActivityUiState),
    dynamicColor = shouldUseDynamicColor(mainActivityUiState)
  ) {

    val newBooksListState by newBooksViewModel.uiState.collectAsStateWithLifecycle()
    val favoriteBooksListState by favoriteBooksViewModel.uiState.collectAsStateWithLifecycle()
    val detailedBookUiState by
    bookDetailsViewModel.bookDetailUiState.collectAsStateWithLifecycle(BookDetailResult.NotFound)

    val appContentCallbacks = newAppContentCallbacks(
      activityContext,
      favoriteBooksViewModel,
      bookDetailsViewModel
    )

    MainScaffold(
        windowSizeClass = windowSizeClass,
        devicePosture = devicePosture,
        appContentCallbacks = appContentCallbacks,
        userPreferencesRepository = userPreferencesRepository,
        bookDetailsViewModel = bookDetailsViewModel,
        newBooksListState = newBooksListState,
        favoriteBooksListState = favoriteBooksListState,
        detailedBookUiState = detailedBookUiState
    )
  }
}


/**
 * Returns true/false if dynamic colors are applied to the ui.
 *
 * @param mainActivityUiState Main activity ui state.
 * @return true/false
 */
@Composable
internal fun shouldUseDynamicColor(
  mainActivityUiState: MainActivityUiState
): Boolean = when (mainActivityUiState) {
  MainActivityUiState.Loading -> false
  is MainActivityUiState.Success -> mainActivityUiState.userData.useDynamicColor
}

/**
 * Returns true/false if dark theme is applied to the ui.
 *
 * @param mainActivityUiState Main activity ui state.
 * @return true/false
 */
@Composable
internal fun shouldUseDarkTheme(
  mainActivityUiState: MainActivityUiState
): Boolean = when (mainActivityUiState) {
  MainActivityUiState.Loading -> isSystemInDarkTheme()
  is MainActivityUiState.Success -> {
    val useDarkTheme = mainActivityUiState.userData.useDarkTheme
    if (useDarkTheme.not()) isSystemInDarkTheme() else useDarkTheme
  }
}
