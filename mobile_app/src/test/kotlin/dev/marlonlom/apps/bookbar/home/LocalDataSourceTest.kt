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

package dev.marlonlom.apps.bookbar.home

import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.released_books.ReleasedBooksDao
import dev.marlonlom.apps.bookbar.utils.RemoteData
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

    private lateinit var dataSource: ReleasedBooksContract.LocalDataSource
    private lateinit var database: AppDatabase
    private lateinit var releasedBooksDao: ReleasedBooksDao

    @Before
    public override fun setUp() {
        database = mock(AppDatabase::class.java)
        releasedBooksDao = mock(ReleasedBooksDao::class.java)
        dataSource = ReleasedBooksContract.LocalDataSource(database)
        `when`(database.releasedBooksDao()).thenReturn(releasedBooksDao)
    }

    @Test
    fun `Should return empty list of released books`() {
        runBlockingTest {
            `when`(releasedBooksDao.listAll()).thenReturn(flowOf(emptyList()))
            val dataSourceResponse = dataSource.listAll().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isNullOrEmpty())
        }
    }

    @Test
    fun `Should return a list of released books`() {
        runBlockingTest {
            val resultsFlow =
                flowOf(RemoteData.releasedBooksApiResponse.books.map { toReleasedBook(it) })
            `when`(releasedBooksDao.listAll()).thenReturn(resultsFlow)
            val dataSourceResponse = dataSource.listAll().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isNotEmpty())
            assertEquals(20, dataSourceResponse.size)
        }
    }
}
