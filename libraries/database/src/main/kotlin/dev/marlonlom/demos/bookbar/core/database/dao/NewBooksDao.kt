/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.marlonlom.demos.bookbar.core.database.entities.NewBookEntity
import kotlinx.coroutines.flow.Flow

/**
 * New books list data access object interface definition.
 *
 * @author marlonlom
 */
@Dao
interface NewBooksDao {

  /**
   * Returns a list with all new books.
   *
   * @return Books list.
   */
  @Query("SELECT * FROM new_it_book ORDER BY creation_date")
  fun getAll(): Flow<List<NewBookEntity>>

  /**
   * Adds or Updates new books.
   *
   * @param books New books
   */
  @Upsert
  suspend fun upsertAll(vararg books: NewBookEntity)

  /** Deletes all new books. */
  @Query("DELETE FROM new_it_book")
  fun clear()
}
