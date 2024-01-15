/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * New Release books entity data class.
 *
 * @author marlonlom
 *
 * @property title Book title.
 * @property isbn13 Book id as isb13.
 * @property price Book price as text.
 * @property image Book image url.
 * @property creationDate Creation data as millis.
 */
@Entity("new_it_book")
data class NewBookEntity(
  @PrimaryKey val isbn13: String,
  val title: String,
  val price: String,
  val image: String,
  @ColumnInfo("creation_date")
  var creationDate: Long = System.currentTimeMillis()
)

/**
 * Favorite book entity data class.
 *
 * @author marlonlom
 *
 * @property title Book title.
 * @property isbn13 Book id as isb13.
 * @property price Book price as text.
 * @property image Book image url.
 * @property creationDate Creation data as millis.
 */
@Entity("favorite_it_book")
data class FavoriteBookEntity(
  @PrimaryKey val isbn13: String,
  val title: String,
  val price: String,
  val image: String,
  @ColumnInfo("creation_date")
  var creationDate: Long = System.currentTimeMillis()
)

/**
 * Book detail entity data class.
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
@Entity("it_book_detail")
data class BookDetailEntity(
  @PrimaryKey val isbn13: String,
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
  val url: String
)
