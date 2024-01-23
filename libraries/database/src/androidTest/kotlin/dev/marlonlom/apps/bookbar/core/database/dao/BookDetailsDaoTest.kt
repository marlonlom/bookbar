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
import dev.marlonlom.apps.bookbar.core.database.entities.BookDetailEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class BookDetailsDaoTest {

  @get:Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var dao: BookDetailsDao
  private lateinit var db: BookbarDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, BookbarDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    dao = db.bookDetailsDao()
  }

  @After
  fun teardown() {
    db.close()
  }

  @Test
  fun shouldNotFindBookUsingIsbn13() = runTest {
    val foundItem: BookDetailEntity = dao.getBook("9781098106225").first()
    assertThat(foundItem).isNull()
  }

  @Test
  fun shouldFindBookUsingIsbn13AfterInsertingIt() = runTest {
    val entity = BookDetailEntity(
      title = "Reliable Machine Learning",
      subtitle = "Applying SRE Principles to ML in Production",
      authors = "Cathy Chen, Niall Richard Murphy, Kranti Parisa, D. Sculley, Todd Underwood",
      publisher = "O'Reilly Media",
      language = "English",
      isbn10 = "1098106229",
      isbn13 = "9781098106225",
      pages = "408",
      year = "2022",
      rating = "4",
      desc = "Whether you&#039;re part of a small startup or a multinational corporation, this practical book shows data scientists, software and site reliability engineers, product managers, and business owners how to run and establish ML reliably, effectively, and accountably within your organization. You&#039;...",
      price = "$43.99",
      image = "https://itbook.store/img/books/9781098106225.png",
      url = "https://itbook.store/books/9781098106225"
    )
    dao.save(entity)
    val foundItem: BookDetailEntity = dao.getBook("9781098106225").first()
    assertThat(foundItem).isNotNull()
  }

}
