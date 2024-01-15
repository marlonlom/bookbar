/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.domain.books

import dev.marlonlom.demos.bookbar.core.database.BookbarDatabase
import dev.marlonlom.demos.bookbar.core.network.BookDetailsApiResponse
import dev.marlonlom.demos.bookbar.core.network.BookStoreApiService
import dev.marlonlom.demos.bookbar.core.network.NewBooksApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Bookstore repository class.
 *
 * @author marlonlom
 *
 * @property bookstoreWebApi Bookstore web api dependency.
 * @property bookstoreDatabase Bookstore local database.
 */
class BookstoreRepository(
  private val bookstoreDatabase: BookbarDatabase,
  private val bookstoreWebApi: BookStoreApiService
) {

  /** New books listing as flow. */
  val newBooksFlow: Flow<List<BooksListDomainItem>> = flow {
    val newBooksFromLocal = bookstoreDatabase.newBooksDao().getAll().first()
    when {
      newBooksFromLocal.isEmpty() -> {
        val apiResponse: NewBooksApiResponse = bookstoreWebApi.getNewBooks()

        when (apiResponse.error) {
          "0" -> {
            val listItems = apiResponse.books.map {
              BooksListDomainItem(
                isbn13 = it.isbn13,
                title = it.title,
                price = it.price,
                image = it.image
              )
            }.also { items ->
              bookstoreDatabase.newBooksDao().upsertAll(
                *items.map { it.toNewBookEntity() }.toTypedArray()
              )
            }
            emit(listItems)
          }

          else -> {
            emit(emptyList())
          }
        }
      }

      else -> {
        emit(
          newBooksFromLocal.map { it.toDomain() }
        )
      }
    }

  }

  /** Favorite books listing as flow. */
  val favoriteBooksFlow: Flow<List<BooksListDomainItem>> = flow {
    emit(bookstoreDatabase.favoriteBooksDao().getAll().first().map {
      it.toDomain()
    })
  }

  /**
   * Finds a book using its id.
   *
   * @param bookId Book isbn13 identifier.
   *
   * @return Found book detail.
   */
  fun findBook(bookId: String): Flow<BookDetailResult> = flow {
    when (val detailEntity = bookstoreDatabase.bookDetailsDao().getBook(bookId).first()) {
      null -> {
        findBookFromNetwork(bookId) { book -> saveBookDetailToLocal(book) }
      }

      else -> {
        emit(BookDetailResult.Success(detailEntity.toDomain()))
      }
    }
  }

  private suspend fun saveBookDetailToLocal(book: BookDetailItem) {
    withContext(Dispatchers.IO) {
      Timber.d("Book[${book.isbn13}] Should be saved into database.")
      bookstoreDatabase.bookDetailsDao().save(book.toNewBookEntity())
    }
  }

  private suspend fun FlowCollector<BookDetailResult>.findBookFromNetwork(
    bookId: String,
    onBookDetailFound: suspend (BookDetailItem) -> Unit
  ) {
    val bookFromNetwork: BookDetailsApiResponse = bookstoreWebApi.getBook(bookId)
    when (bookFromNetwork.error) {
      "0" -> {
        val bookDetailItem = bookFromNetwork.toDomain().also { item ->
          onBookDetailFound(item)
        }
        emit(BookDetailResult.Success(bookDetailItem))
      }

      else -> {
        emit(BookDetailResult.NotFound)
      }
    }

  }

  suspend fun isFavoriteBook(bookId: String): Flow<Boolean> = flow {
    bookstoreDatabase.favoriteBooksDao().ifFavoriteBook(bookId).collect {
      emit(it > 0)
    }
  }

  suspend fun saveFavoriteBook(itemAsFavorite: BooksListDomainItem) {
    val favoriteBookEntity = itemAsFavorite.toFavoriteBookEntity()
    bookstoreDatabase.favoriteBooksDao().save(favoriteBookEntity)
  }

  /**
   * Removes selected book id from favorite books.
   *
   * @param bookId Book id as isbn13.
   *
   */
  suspend fun removeFavoriteBook(bookId: String) {
    bookstoreDatabase.favoriteBooksDao().deleteByBookId(bookId)
  }
}
