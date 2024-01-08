/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.marlonlom.demos.bookbar.core.database.dao.FavoriteBooksDao
import dev.marlonlom.demos.bookbar.core.database.dao.NewBooksDao
import dev.marlonlom.demos.bookbar.core.database.entities.FavoriteBookEntity
import dev.marlonlom.demos.bookbar.core.database.entities.NewBookEntity

/**
 * IT Bookstore database abstract class.
 *
 * @author marlonlom
 */
@Database(
  version = 1,
  exportSchema = false,
  entities = [
    NewBookEntity::class,
    FavoriteBookEntity::class
  ]
)
abstract class BookbarDatabase : RoomDatabase() {

  /**
   * Returns the new books dao.
   *
   * @return New books dao.
   */
  abstract fun newBooksDao(): NewBooksDao

  /**
   * Returns the favorite books dao.
   *
   * @return Favorite books dao.
   */
  abstract fun favoriteBooksDao(): FavoriteBooksDao

  companion object {

    private const val DATABASE_NAME = "bookbar-db"

    @Volatile
    private var instance: BookbarDatabase? = null

    /**
     * Creates a singleton instance of database.
     *
     * @param context application context.
     *
     * @return room database instance.
     */
    fun getInstance(context: Context): BookbarDatabase {
      return instance ?: synchronized(this) {
        instance ?: buildDatabase(context).also { instance = it }
      }
    }

    /**
     * Creates a Room database instance.
     *
     * @param context Application context.
     *
     * @return Room database instance for the app.
     */
    private fun buildDatabase(context: Context): BookbarDatabase = Room.databaseBuilder(
      context,
      BookbarDatabase::class.java,
      DATABASE_NAME
    ).fallbackToDestructiveMigration()
      .build()

  }
}
