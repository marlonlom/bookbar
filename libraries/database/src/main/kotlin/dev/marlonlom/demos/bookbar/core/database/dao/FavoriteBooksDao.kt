/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.marlonlom.demos.bookbar.core.database.entities.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow

/**
 * Favorite books data access object interface definition.
 *
 * @author marlonlom
 */
@Dao
interface FavoriteBooksDao {

  /**
   * Returns a list with all favorite books.
   *
   * @return Books list.
   */
  @Query("SELECT * FROM favorite_it_book ORDER BY creation_date")
  fun getAll(): Flow<List<FavoriteBookEntity>>

  /**
   * Returns the count of existence of favorite book using its isbn13.
   *
   * @param bookId Book id.
   *
   * @return 0 if not result, 1 if yes.
   */
  @Query("SELECT COUNT(fb.isbn13) FROM favorite_it_book fb WHERE fb.isbn13 = :bookId")
  fun ifFavoriteBook(bookId: String): Flow<Int>


  /**
   * Adds a book as favorite.
   *
   * @param book Favorite book.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun save(book: FavoriteBookEntity)

  /**
   * Delete favorite book using its isbn13.
   *
   * @param bookId Book id.
   */
  @Query("DELETE FROM favorite_it_book WHERE isbn13 = :bookId")
  suspend fun deleteByBookId(bookId: String)

}
