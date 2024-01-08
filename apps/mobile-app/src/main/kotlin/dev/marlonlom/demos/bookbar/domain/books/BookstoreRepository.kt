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
}

/**
 * Books list item domain data class.
 *
 * @author marlonlom
 *
 * @property title Book title.
 * @property isbn13 Book id as isb13.
 * @property price Book price as text.
 * @property image Book image url.
 */
data class BooksListItem(
  val isbn13: String,
  val title: String,
  val price: String,
  val image: String,
)
