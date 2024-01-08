/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.database.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.marlonlom.demos.bookbar.core.database.BookbarDatabase
import dev.marlonlom.demos.bookbar.core.database.entities.NewBookEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewBooksDaoTest {

  @get:Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var dao: NewBooksDao
  private lateinit var db: BookbarDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, BookbarDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    dao = db.newBooksDao()
  }

  @After
  fun teardown() {
    db.close()
  }

  @Test
  fun shouldGetNewBooksEmptyList() = runTest {
    val list: List<NewBookEntity> = dao.getAll().first()
    assertThat(list).isEmpty()
  }

  @Test
  fun shouldGetNewBooksListAfterInsertData() = runTest {
    val entity = NewBookEntity(
      isbn13 = "9781912047451",
      title = "An Introduction to C & GUI Programming, 2nd Edition",
      price = "$14.92",
      image = "https://itbook.store/img/books/9781912047451.png"
    )
    dao.upsertAll(entity)
    val list: List<NewBookEntity> = dao.getAll().first()
    assertThat(list).isNotEmpty()
  }

  @Test
  fun shouldGetNewBooksEmptyListAfterDeletingData() = runTest {
    val entity = NewBookEntity(
      isbn13 = "9781912047451",
      title = "An Introduction to C & GUI Programming, 2nd Edition",
      price = "$14.92",
      image = "https://itbook.store/img/books/9781912047451.png"
    )
    dao.upsertAll(entity)
    dao.clear()
    val list: List<NewBookEntity> = dao.getAll().first()
    assertThat(list).isEmpty()
  }

}
