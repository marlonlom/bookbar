/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * New books api response data class.
 *
 * @author marlonlom
 *
 * @property error If success, return '0', otherwise return short text.
 * @property total Total results as text.
 * @property books Book list items from response.
 */
@Serializable
data class NewBooksApiResponse(
  @SerialName("error") val error: String,
  @SerialName("total") val total: String,
  @SerialName("books") val books: ArrayList<BookListItem> = arrayListOf()
)

/**
 * Book list item api response data class.
 *
 * @author marlonlom
 *
 * @property title Book title.
 * @property subtitle Book subtitle.
 * @property isbn13 Book id as isb13.
 * @property price Book price as text.
 * @property image Book image url.
 * @property url Book url.
 */
@Serializable
data class BookListItem(
  @SerialName("title") val title: String,
  @SerialName("subtitle") val subtitle: String,
  @SerialName("isbn13") val isbn13: String,
  @SerialName("price") val price: String,
  @SerialName("image") val image: String,
  @SerialName("url") val url: String
)

/**
 * Book details api response data class.
 *
 * @author marlonlom
 *
 * @property error If success, return '0', otherwise return short text.
 * @property title Book title.
 * @property subtitle Book subtitle.
 * @property authors Book author names.
 * @property publisher Book publisher name.
 * @property language Book language name.
 * @property isbn10 Book id as isb10.
 * @property isbn13 Book id as isb13.
 * @property pages Book total pages.
 * @property year Book published year.
 * @property rating Book rating value.
 * @property desc Book description.
 * @property price Book price as text.
 * @property image Book image url.
 * @property url Book url.
 */
@Serializable
data class BookDetailsApiResponse(
  @SerialName("error") val error: String,
  @SerialName("title") val title: String? = null,
  @SerialName("subtitle") val subtitle: String? = null,
  @SerialName("authors") val authors: String? = null,
  @SerialName("publisher") val publisher: String? = null,
  @SerialName("language") val language: String? = null,
  @SerialName("isbn10") val isbn10: String? = null,
  @SerialName("isbn13") val isbn13: String? = null,
  @SerialName("pages") val pages: String? = null,
  @SerialName("year") val year: String? = null,
  @SerialName("rating") val rating: String? = null,
  @SerialName("desc") val desc: String? = null,
  @SerialName("price") val price: String? = null,
  @SerialName("image") val image: String? = null,
  @SerialName("url") val url: String? = null
)
