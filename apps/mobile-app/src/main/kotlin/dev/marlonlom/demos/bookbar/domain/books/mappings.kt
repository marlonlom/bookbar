/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.domain.books

import dev.marlonlom.demos.bookbar.core.database.entities.BookDetailEntity
import dev.marlonlom.demos.bookbar.core.database.entities.FavoriteBookEntity
import dev.marlonlom.demos.bookbar.core.database.entities.NewBookEntity
import dev.marlonlom.demos.bookbar.core.network.BookDetailsApiResponse

/**
 * Extension function for converting book details api response to domain model.
 *
 * @return Book details domain object.
 */
internal fun BookDetailEntity.toDomain(): BookDetailItem = BookDetailItem(
  isbn13 = this.isbn13,
  isbn10 = this.isbn10,
  title = this.title,
  subtitle = this.subtitle,
  authors = this.authors,
  publisher = this.publisher,
  language = this.language,
  pages = this.pages,
  year = this.year,
  rating = this.rating,
  desc = this.desc,
  price = this.price,
  image = this.image,
  url = this.url
)

/**
 * Extension function for converting book details domain response to entity model.
 *
 * @return Book details entity object.
 */
internal fun BookDetailItem.toNewBookEntity() = BookDetailEntity(
  isbn13 = this.isbn13,
  isbn10 = this.isbn10,
  title = this.title,
  subtitle = this.subtitle,
  authors = this.authors,
  publisher = this.publisher,
  language = this.language,
  pages = this.pages,
  year = this.year,
  rating = this.rating,
  desc = this.desc,
  price = this.price,
  image = this.image,
  url = this.url
)

/**
 * Extension function for converting book details api response to domain model.
 *
 * @return Book details domain object.
 */
internal fun BookDetailsApiResponse.toDomain(): BookDetailItem = BookDetailItem(
  isbn13 = this.isbn13!!,
  isbn10 = this.isbn10!!,
  title = this.title!!,
  subtitle = this.subtitle!!,
  authors = this.authors!!,
  publisher = this.publisher!!,
  language = this.language!!,
  pages = this.pages!!,
  year = this.year!!,
  rating = this.rating!!,
  desc = this.desc!!,
  price = this.price!!,
  image = this.image!!,
  url = this.url!!
)

/**
 * Extension function for converting book list domain item to new book entity model.
 *
 * @author marlonlom
 */
internal fun BooksListDomainItem.toNewBookEntity() = NewBookEntity(
  isbn13 = this.isbn13,
  title = this.title,
  price = this.price,
  image = this.image
)

/**
 * Extension function for converting book list domain item to favorite book entity model.
 *
 * @author marlonlom
 */
internal fun BooksListDomainItem.toFavoriteBookEntity() = FavoriteBookEntity(
  isbn13 = this.isbn13,
  title = this.title,
  price = this.price,
  image = this.image
)

/**
 * Extension function for converting favorite book entity item to domain model.
 *
 * @author marlonlom
 */
internal fun FavoriteBookEntity.toDomain() = BooksListDomainItem(
  isbn13 = this.isbn13,
  title = this.title,
  price = this.price,
  image = this.image
)

/**
 * Extension function for converting new book entity item to domain model.
 *
 * @author marlonlom
 */
internal fun NewBookEntity.toDomain() = BooksListDomainItem(
  isbn13 = this.isbn13,
  title = this.title,
  price = this.price,
  image = this.image
)
