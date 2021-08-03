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

import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.model.network.BooksListApiResponse
import dev.marlonlom.apps.bookbar.utils.RemoteData
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest : TestCase() {

    private lateinit var dataSource: ReleasedBooksContract.RemoteDataSource
    private lateinit var api: BookStoreApi

    @Before
    public override fun setUp() {
        api = mock(BookStoreApi::class.java)
        dataSource = ReleasedBooksContract.RemoteDataSource(api)
    }

    @Test
    fun `Should return empty list of released books`() {
        runBlockingTest {
            `when`(api.getNewBooks()).thenReturn(BooksListApiResponse(emptyList(), "0", "0"))
            val dataSourceResponse = dataSource.retrieveNewBooks().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isFailure)
            assertEquals(
                "Released books not found.",
                dataSourceResponse.exceptionOrNull()!!.message
            )
        }
    }

    @Test
    fun `Should return a list of released books`() {
        runBlockingTest {
            `when`(api.getNewBooks()).thenReturn(RemoteData.releasedBooksApiResponse)
            val dataSourceResponse = dataSource.retrieveNewBooks().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isSuccess)
            assertEquals(20, dataSourceResponse.getOrThrow().size)
        }
    }
}
