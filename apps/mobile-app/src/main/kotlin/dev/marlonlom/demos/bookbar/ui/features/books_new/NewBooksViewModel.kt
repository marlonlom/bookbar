/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.books_new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.demos.bookbar.domain.books.BooksListItem
import dev.marlonlom.demos.bookbar.domain.books.BookstoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

/**
 * New books list fetch viewmodel.
 *
 * @author marlonlom
 *
 * @property repository Bookstore repository.
 */
class NewBooksViewModel(
  private val repository: BookstoreRepository
) : ViewModel() {

  val uiState: StateFlow<NewBooksUiState> = repository.newBooksFlow
    .map { books ->
      when {
        books.isNotEmpty() -> NewBooksUiState.Success(books)
        else -> NewBooksUiState.Empty
      }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
      initialValue = NewBooksUiState.Loading,
    )

  companion object {
    /**
     * Returns the factory for new books viewmodel.
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
      ): T = NewBooksViewModel(repository) as T

    }

  }
}

/**
 * New books ui state sealed interface.
 *
 * @author marlonlom
 *
 */
sealed interface NewBooksUiState {

  /**
   * Loading phase for New books ui state.
   *
   * @author marlonlom
   */
  data object Loading : NewBooksUiState

  /**
   * Empty phase for New books ui state.
   *
   * @author marlonlom
   */
  data object Empty : NewBooksUiState

  /**
   * Loading phase for New books ui state.
   *
   * @author marlonlom
   *
   * @property books Books list.
   */
  data class Success(
    val books: List<BooksListItem>
  ) : NewBooksUiState
}
