/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.domain.books

import dev.marlonlom.apps.bookbar.BuildConfig

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
data class BooksListDomainItem(
  val isbn13: String,
  val title: String,
  val price: String,
  val image: String,
)

/**
 * Book detail domain data class.
 *
 * @author marlonlom
 *
 * @property isbn13 Book id as isb13.
 * @property isbn10 Book id as isb10.
 * @property title Book title.
 * @property subtitle Book subtitle.
 * @property authors Book author names.
 * @property publisher Book publisher name.
 * @property language Book language name.
 * @property pages Book total pages.
 * @property year Book published year.
 * @property rating Book rating value.
 * @property desc Book description.
 * @property price Book price as text.
 * @property image Book image url.
 * @property url Book url.
 */
data class BookDetailItem(
  val isbn13: String,
  val isbn10: String,
  val title: String,
  val subtitle: String,
  val authors: String,
  val publisher: String,
  val language: String,
  val pages: String,
  val year: String,
  val rating: String,
  val desc: String,
  val price: String,
  val image: String,
  val url: String,
  val favorite: Boolean = false
) {
  /** Book url for buying purposes. */
  val buyUrl: String get() = "${BuildConfig.ITBOOKSTORE_BUY_URL}/$isbn13"
}

/**
 * Book details result sealed class.
 *
 * @author marlonlom
 *
 */
sealed class BookDetailResult {

  /** Loading phase of book detail result. */
  data object Loading : BookDetailResult()

  /** Failure phase of book detail result. */
  data object NotFound : BookDetailResult()

  /**
   * Success phase of book detail result.
   *
   * @property item Book detail.
   */
  data class Success(val item: BookDetailItem) : BookDetailResult()
}

