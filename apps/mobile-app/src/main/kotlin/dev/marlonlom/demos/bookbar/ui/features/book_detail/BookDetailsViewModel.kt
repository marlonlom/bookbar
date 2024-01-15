/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.book_detail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.demos.bookbar.domain.books.BookDetailItem
import dev.marlonlom.demos.bookbar.domain.books.BookDetailResult
import dev.marlonlom.demos.bookbar.domain.books.BookDetailResult.Success
import dev.marlonlom.demos.bookbar.domain.books.BooksListDomainItem
import dev.marlonlom.demos.bookbar.domain.books.BookstoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

@VisibleForTesting
const val SELECTED_BOOK_KEY = "selectedBook"

/**
 * Book details view model class.
 *
 * @author marlonlom
 *
 * @param savedStateHandle Saved state handle.
 * @property bookstoreRepository Bookstore repository.
 *
 * @constructor
 * Constructs a Book details view model.
 *
 */
@ExperimentalCoroutinesApi
class BookDetailsViewModel(
  private val savedStateHandle: SavedStateHandle,
  private val bookstoreRepository: BookstoreRepository
) : ViewModel() {

  /** Book details ui state. */
  val bookDetailUiState: Flow<BookDetailResult> = savedStateHandle
    .getStateFlow(SELECTED_BOOK_KEY, "")
    .transformLatest { bookId ->
      emit(BookDetailResult.Loading)
      if (bookId.isEmpty()) {
        emit(BookDetailResult.NotFound)
      }
      combine(
        bookstoreRepository.findBook(bookId),
        bookstoreRepository.isFavoriteBook(bookId)
      ) { bookDetailResult: BookDetailResult, isFavorite: Boolean ->
        return@combine when (bookDetailResult) {
          is Success -> Success(
            item = bookDetailResult.item.copy(favorite = isFavorite)
          )

          else -> bookDetailResult
        }
      }.collect { detailResult: BookDetailResult ->
        emit(detailResult)
      }
    }

  /**
   * Save the selected book id in state handle.
   *
   * @param bookId Book id as isbn13.
   */
  fun setSelectedBook(bookId: String) {
    savedStateHandle[SELECTED_BOOK_KEY] = bookId
  }

  /**
   * Saves the favorite book information for provided book details.
   *
   * @param bookDetailItem Book detail item.
   * @param markFavorite True/False for book to be marked as favorite.
   */
  fun toggleFavorite(bookDetailItem: BookDetailItem, markFavorite: Boolean) {
    viewModelScope.launch {
      if (markFavorite) {
        val itemAsFavorite = BooksListDomainItem(
          isbn13 = bookDetailItem.isbn13,
          title = bookDetailItem.title,
          price = bookDetailItem.price,
          image = bookDetailItem.image
        )
        bookstoreRepository.saveFavoriteBook(itemAsFavorite)
        setSelectedBook(bookDetailItem.isbn13)
      } else {
        bookstoreRepository.removeFavoriteBook(bookDetailItem.isbn13)
        setSelectedBook(bookDetailItem.isbn13)
      }
    }
  }

  companion object {
    /**
     * Returns the factory for book details viewmodel.
     *
     * @param repository BookStore repository.
     */
    @JvmStatic
    fun factory(
      repository: BookstoreRepository
    ) = object : ViewModelProvider.Factory {

      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel> create(
        modelClass: Class<T>
      ): T = BookDetailsViewModel(
        savedStateHandle = SavedStateHandle(),
        bookstoreRepository = repository
      ) as T

    }

  }
}
