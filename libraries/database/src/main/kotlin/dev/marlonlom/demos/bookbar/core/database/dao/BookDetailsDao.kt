/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.marlonlom.demos.bookbar.core.database.entities.BookDetailEntity
import kotlinx.coroutines.flow.Flow

/**
 * Book details data access object interface definition.
 *
 * @author marlonlom
 */
@Dao
interface BookDetailsDao {

  /**
   * Returns book detail using its isbn13.
   *
   * @param bookId Book id.
   *
   * @return Found book detail.
   */
  @Query("SELECT * FROM book_detail WHERE isbn13 = :bookId")
  fun getBook(bookId: String): Flow<BookDetailEntity>

  /**
   * Adds book detailed information.
   *
   * @param book Book detail.
   */
  @Upsert
  suspend fun save(book: BookDetailEntity)

}
