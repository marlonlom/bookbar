/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.database.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.marlonlom.apps.bookbar.core.database.BookbarDatabase
import dev.marlonlom.apps.bookbar.core.database.entities.FavoriteBookEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteBooksDaoTest {

  @get:Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var dao: FavoriteBooksDao
  private lateinit var db: BookbarDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, BookbarDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    dao = db.favoriteBooksDao()
  }

  @After
  fun teardown() {
    db.close()
  }

  @Test
  fun shouldGetFavoriteBooksEmptyList() = runTest {
    val list: List<FavoriteBookEntity> = dao.getAll().first()
    assertThat(list).isEmpty()
  }

  @Test
  fun shouldGetFavoriteBooksListAfterInsertData() = runTest {
    val favoriteBook = FavoriteBookEntity(
      isbn13 = "9781912047451",
      title = "An Introduction to C & GUI Programming, 2nd Edition",
      price = "$14.92",
      image = "https://itbook.store/img/books/9781912047451.png"
    )
    dao.save(favoriteBook)
    val list: List<FavoriteBookEntity> = dao.getAll().first()
    assertThat(list).isNotEmpty()
  }

  @Test
  fun shouldGetFavoriteBooksEmptyListAfterDeletingData() = runTest {
    val favoriteBook = FavoriteBookEntity(
      isbn13 = "9781912047451",
      title = "An Introduction to C & GUI Programming, 2nd Edition",
      price = "$14.92",
      image = "https://itbook.store/img/books/9781912047451.png"
    )
    dao.save(favoriteBook)
    dao.deleteByBookId(favoriteBook.isbn13)
    val list: List<FavoriteBookEntity> = dao.getAll().first()
    assertThat(list).isEmpty()
  }

}
