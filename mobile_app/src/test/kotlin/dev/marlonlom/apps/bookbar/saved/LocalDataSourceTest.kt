/*
 * Copyright (c) 2021 marlonlom.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.marlonlom.apps.bookbar.saved

import dev.marlonlom.apps.bookbar.detail.toBookDetailEntity
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetailsDao
import dev.marlonlom.apps.bookbar.saved.SavedBooksContract.LocalDataSource
import dev.marlonlom.apps.bookbar.utils.RemoteData.savedBooksApiResponse
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceTest : TestCase() {

    private lateinit var dataSource: LocalDataSource
    private lateinit var database: AppDatabase
    private lateinit var bookDetailsDao: BookDetailsDao

    @Before
    public override fun setUp() {
        database = mock(AppDatabase::class.java)
        bookDetailsDao = mock(BookDetailsDao::class.java)
        dataSource = LocalDataSource(database)
        `when`(database.bookDetailsDao()).thenReturn(bookDetailsDao)
    }

    @Test
    fun `Should return empty list of saved books`() {
        runBlockingTest {
            `when`(bookDetailsDao.listSaved()).thenReturn(flowOf(emptyList()))
            val dataSourceResponse = dataSource.listSaved().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isNullOrEmpty())
        }
    }

    @Test
    fun `Should return a list of saved books`() {
        runBlockingTest {
            val resultsFlow = flowOf(savedBooksApiResponse.books.map { toBookDetailEntity(it) })
            `when`(bookDetailsDao.listSaved()).thenReturn(resultsFlow)
            val dataSourceResponse = dataSource.listSaved().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isNotEmpty())
            assertEquals(2, dataSourceResponse.size)
        }
    }

    @Test
    fun `Should save book state for selected isbn`() {
        runBlockingTest {
            dataSource.toggleSaved("1001622115721", true)
        }
    }

    @Test
    fun `Should unsave book state for selected isbn`() {
        runBlockingTest {
            dataSource.toggleSaved("1001622115721", false)
        }
    }

}
