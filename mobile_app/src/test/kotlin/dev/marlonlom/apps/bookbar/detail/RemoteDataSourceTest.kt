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

package dev.marlonlom.apps.bookbar.detail

import dev.marlonlom.apps.bookbar.detail.BookDetailsContract.RemoteDataSource
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.utils.RemoteData
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
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
    fun `Should not return book detail using selected isbn`() {
        runBlockingTest {
            val errorApiResponse = RemoteData.freeBookApiResponse.copy(error = "999")
            `when`(api.getBookDetail(Mockito.anyString())).thenReturn(errorApiResponse)
            val response = dataSource.findBook("9781484266835").first()
            assertNotNull(response)
            assertTrue(response.isFailure)
            assertEquals("Book information not found.", response.exceptionOrNull()!!.message)
        }
    }

    @Test
    fun `Should return free book detail using selected isbn`() {
        runBlockingTest {
            val bookItem = RemoteData.freeBookApiResponse
            `when`(api.getBookDetail(Mockito.anyString())).thenReturn(bookItem)
            val response = dataSource.findBook(bookItem.isbn13!!).first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(bookItem.error, response.getOrThrow().error)
            assertEquals(bookItem.isbn10, response.getOrThrow().isbn10)
            assertEquals(bookItem.isbn13, response.getOrThrow().isbn13)
            assertEquals(bookItem.title, response.getOrThrow().title)
            assertEquals(bookItem.subtitle, response.getOrThrow().subtitle)
            assertEquals(bookItem.desc, response.getOrThrow().desc)
            assertEquals(bookItem.image, response.getOrThrow().image)
            assertEquals(bookItem.url, response.getOrThrow().url)
            assertEquals(bookItem.year, response.getOrThrow().year)
            assertEquals(bookItem.rating, response.getOrThrow().rating)
            assertEquals(bookItem.price, response.getOrThrow().price)
            assertEquals(bookItem.pages, response.getOrThrow().pages)
            assertEquals(bookItem.language, response.getOrThrow().language)
            assertEquals(bookItem.authors, response.getOrThrow().authors)
            assertEquals(bookItem.publisher, response.getOrThrow().publisher)
            assertNotNull(response.getOrThrow().pdf)
            assertTrue(response.getOrThrow().pdf!!.hasFreeEBook)
            assertEquals(bookItem.pdf!!.freeEBook, response.getOrThrow().pdf!!.freeEBook)
        }
    }

    @Test
    fun `Should return purchasable book detail using selected isbn`() {
        runBlockingTest {
            val bookItem = RemoteData.purchasableBookApiResponse
            `when`(api.getBookDetail(Mockito.anyString())).thenReturn(bookItem)
            val response = dataSource.findBook(bookItem.isbn13!!).first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(bookItem.error, response.getOrThrow().error)
            assertEquals(bookItem.isbn10, response.getOrThrow().isbn10)
            assertEquals(bookItem.isbn13, response.getOrThrow().isbn13)
            assertEquals(bookItem.title, response.getOrThrow().title)
            assertEquals(bookItem.subtitle, response.getOrThrow().subtitle)
            assertEquals(bookItem.desc, response.getOrThrow().desc)
            assertEquals(bookItem.image, response.getOrThrow().image)
            assertEquals(bookItem.url, response.getOrThrow().url)
            assertEquals(bookItem.year, response.getOrThrow().year)
            assertEquals(bookItem.rating, response.getOrThrow().rating)
            assertEquals(bookItem.price, response.getOrThrow().price)
            assertEquals(bookItem.pages, response.getOrThrow().pages)
            assertEquals(bookItem.language, response.getOrThrow().language)
            assertEquals(bookItem.authors, response.getOrThrow().authors)
            assertEquals(bookItem.publisher, response.getOrThrow().publisher)
            assertNull(response.getOrThrow().pdf)
        }
    }
}
