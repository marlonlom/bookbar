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

package dev.marlonlom.apps.bookbar.search

import dev.marlonlom.apps.bookbar.model.network.BookSearchApiResponse
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.search.SearchedBooksContract.RemoteDataSource
import dev.marlonlom.apps.bookbar.utils.RemoteData.searchedKotlinBooksApiResponse
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest : TestCase() {

    private lateinit var dataSource: RemoteDataSource
    private lateinit var api: BookStoreApi

    @Before
    public override fun setUp() {
        api = mock(BookStoreApi::class.java)
        dataSource = RemoteDataSource(api)
    }

    @Test
    fun `Should not return search results using text at page 1`() {
        runBlocking {
            `when`(api.search(anyString(), anyString()))
                .thenReturn(BookSearchApiResponse.EMPTY_RESPONSE)
            val response = dataSource.searchBooks("kotlin").first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().books!!.isNullOrEmpty())
            assertEquals("1", response.getOrThrow().page)
            assertEquals("0", response.getOrThrow().total)
        }
    }

    @Test
    fun `Should return search result error using text at page 1`() {
        runBlocking {
            `when`(api.search(anyString(), anyString())).thenReturn(null)
            val response = dataSource.searchBooks("").first()
            assertNotNull(response)
            assertTrue(response.isFailure)
            assertEquals("Searched books not found.", response.exceptionOrNull()!!.message)
        }
    }

    @Test
    fun `Should return search results using text at page 1`() {
        runBlocking {
            `when`(api.search(anyString(), anyString())).thenReturn(searchedKotlinBooksApiResponse)
            val response = dataSource.searchBooks("kotlin").first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().books!!.isNotEmpty())
            assertEquals("1", response.getOrThrow().page)
            assertEquals("16", response.getOrThrow().total)
        }
    }

}
