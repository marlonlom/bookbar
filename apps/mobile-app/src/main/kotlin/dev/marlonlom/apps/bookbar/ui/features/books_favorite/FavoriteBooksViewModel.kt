/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.features.books_favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.apps.bookbar.domain.books.BooksListDomainItem
import dev.marlonlom.apps.bookbar.domain.books.BookstoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * Favorite books list fetch viewmodel.
 *
 * @author marlonlom
 *
 * @property repository Bookstore repository.
 */
class FavoriteBooksViewModel(
  private val repository: BookstoreRepository
) : ViewModel() {

  private val _uiState: MutableStateFlow<FavoriteBooksUiState> = MutableStateFlow(FavoriteBooksUiState.Empty)
  val uiState: StateFlow<FavoriteBooksUiState> = _uiState.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
    initialValue = FavoriteBooksUiState.Loading,
  )

  init {
    viewModelScope.launch {
      fetchFavoriteBooks()
    }
  }

  /**
   * Removes selected book isbn13 as favorite.
   *
   * @param isbn13 Book id as isbn13.
   */
  fun removeFavorite(isbn13: String) {
    viewModelScope.launch {
      repository.removeFavoriteBook(isbn13)
      fetchFavoriteBooks()
    }
  }

  /** Fetch favorite books from repository. */
  private suspend fun fetchFavoriteBooks() {
    repository.favoriteBooksFlow
      .map { books ->
        when {
          books.isNotEmpty() -> FavoriteBooksUiState.Success(books)
          else -> FavoriteBooksUiState.Empty
        }
      }.collect { favoriteBooksUiState ->
        _uiState.update { favoriteBooksUiState }
      }
  }

  companion object {
    /**
     * Returns the factory for favorite books viewmodel.
     *
     * @param repository
     */
    @JvmStatic
    fun factory(
      repository: BookstoreRepository
    ) = object : ViewModelProvider.Factory {

      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel> create(
        modelClass: Class<T>
      ): T = FavoriteBooksViewModel(repository) as T

    }

  }
}

/**
 * Favorite books ui state sealed interface.
 *
 * @author marlonlom
 *
 */
sealed interface FavoriteBooksUiState {

  /**
   * Loading phase for Favorite books ui state.
   *
   * @author marlonlom
   */
  data object Loading : FavoriteBooksUiState

  /**
   * Empty phase for Favorite books ui state.
   *
   * @author marlonlom
   */
  data object Empty : FavoriteBooksUiState

  /**
   * Loading phase for Favorite books ui state.
   *
   * @author marlonlom
   *
   * @property books Books list.
   */
  data class Success(
    val books: List<BooksListDomainItem>
  ) : FavoriteBooksUiState
}
