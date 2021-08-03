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

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingSource.LoadResult.Page
import dev.marlonlom.apps.bookbar.model.network.BookSearchApiResponse.Companion.EMPTY_RESPONSE
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.search.SearchedBooksContract.RemoteDataSource
import dev.marlonlom.apps.bookbar.utils.RemoteData.searchedKotlinBooksApiResponse
import junit.framework.TestCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@InternalCoroutinesApi
@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest : TestCase() {

    private lateinit var dataSource: RemoteDataSource
    private lateinit var api: BookStoreApi

    @Before
    public override fun setUp() {
        api = mock(BookStoreApi::class.java)
    }

    @Test
    fun `Should not return search results using text at page 1`() {
        runBlockingTest {
            `when`(api.search(anyString(), anyString()))
                .thenReturn(EMPTY_RESPONSE)
            val params: LoadParams<Int> = LoadParams.Refresh(1, 20, false)
            val expectedResult = Page(
                data = EMPTY_RESPONSE.books!!,
                prevKey = null,
                nextKey = null
            )
            dataSource = RemoteDataSource(api, "anything")
            val response = dataSource.load(params)
            assertTrue(response is Page)
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `Should return search results using text at page 1`() {
        runBlockingTest {
            `when`(api.search(anyString(), anyString())).thenReturn(searchedKotlinBooksApiResponse)
            val params: LoadParams<Int> = LoadParams.Refresh(1, 20, false)
            val expectedResult = Page(
                data = searchedKotlinBooksApiResponse.books!!,
                prevKey = null,
                nextKey = 2
            )
            dataSource = RemoteDataSource(api, "kotlin")
            val response = dataSource.load(params)
            assertTrue(response is Page)
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `Should return search results error using text at page 1`() {
        runBlockingTest {
            `when`(api.search(anyString(), anyString()))
                .thenReturn(EMPTY_RESPONSE.copy(books = null))
            val params: LoadParams<Int> = LoadParams.Refresh(1, 20, false)
            dataSource = RemoteDataSource(api, "kotlin")
            val response = dataSource.load(params)
            assertTrue(response is Error)
        }
    }

}
