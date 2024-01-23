package dev.marlonlom.apps.bookbar.ui.main

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.marlonlom.apps.bookbar.core.preferences.UserPreferencesRepository
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.ui.features.book_detail.BookDetailsViewModel
import dev.marlonlom.apps.bookbar.ui.features.books_favorite.FavoriteBooksViewModel
import dev.marlonlom.apps.bookbar.ui.features.books_new.NewBooksViewModel
import dev.marlonlom.apps.bookbar.ui.theme.BookbarTheme
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
      userPreferencesRepository = userPreferencesRepository,
      bookDetailsViewModel = bookDetailsViewModel,
      appContentCallbacks = appContentCallbacks,
      newBooksListState = newBooksListState,
      favoriteBooksListState = favoriteBooksListState,
      detailedBookUiState = detailedBookUiState
    )
  }
}