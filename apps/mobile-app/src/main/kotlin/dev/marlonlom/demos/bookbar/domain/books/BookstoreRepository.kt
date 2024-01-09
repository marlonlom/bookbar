/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.domain.books

import dev.marlonlom.demos.bookbar.core.network.BookStoreApiService
import dev.marlonlom.demos.bookbar.core.network.NewBooksApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Bookstore repository.
 *
 * @author marlonlom
 *
 * @property bookstoreWebApi Bookstore web api dependency.
 */
class BookstoreRepository(
  private val bookstoreWebApi: BookStoreApiService
) {

  /** New books listing as flow. */
  val newBooksFlow: Flow<List<BooksListItem>> = flow {
    val apiResponse: NewBooksApiResponse = bookstoreWebApi.getNewBooks()
    emit(
      when (apiResponse.error) {
        "0" -> apiResponse.books.map {
          BooksListItem(
            isbn13 = it.isbn13,
            title = it.title,
            price = it.price,
            image = it.image
          )
        }

        else -> emptyList()
      }
    )
  }

  /** Favorite books listing as flow. */
  val favoriteBooksFlow: Flow<List<BooksListItem>> = flow {
    emit(emptyList())
  }
}
