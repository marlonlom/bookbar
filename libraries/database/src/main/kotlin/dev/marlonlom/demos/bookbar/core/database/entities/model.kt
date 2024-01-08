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
