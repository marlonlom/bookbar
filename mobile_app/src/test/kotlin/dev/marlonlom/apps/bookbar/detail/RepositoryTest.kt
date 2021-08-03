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

import dev.marlonlom.apps.bookbar.detail.BookDetailsContract.*
import dev.marlonlom.apps.bookbar.utils.RemoteData
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest : TestCase() {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var repository: Repository

    @Before
    public override fun setUp() {
        remoteDataSource = mock(RemoteDataSource::class.java)
        localDataSource = mock(LocalDataSource::class.java)
        repository = Repository(localDataSource, remoteDataSource)
    }

    @Test
    fun `Should save book detail state for selected isbn`() {
        runBlocking {
            repository.toggleSaved("1001622115721", true)
        }
    }

    @Test
    fun `Should unsave book detail state for selected isbn`() {
        runBlocking {
            repository.toggleSaved("1001622115721", false)
        }
    }

    @Test
    fun `Should not return book detail using selected isbn`() {
        runBlocking {
            `when`(localDataSource.findSingle(anyString())).thenReturn(flowOf(emptyList()))
            `when`(remoteDataSource.findBook(anyString()))
                .thenReturn(flowOf(Result.failure(Exception("Book information not found."))))

            val response = repository.findBook("9781484266835").first()
            assertNotNull(response)
            assertTrue(response.isFailure)
            assertEquals("Book information not found.", response.exceptionOrNull()!!.message)
        }
    }

    @Test
    fun `Should return free book detail from local data source using selected isbn`() {
        runBlocking {
            val bookItem = RemoteData.freeBookApiResponse
            `when`(localDataSource.findSingle(anyString()))
                .thenReturn(flowOf(listOf(toBookDetailEntity(bookItem))))
            val response = repository.findBook(bookItem.isbn13!!).first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(bookItem.isbn10, response.getOrThrow().isbn10)
            assertEquals(bookItem.isbn13, response.getOrThrow().isbn13)
            assertEquals(bookItem.title, response.getOrThrow().title)
            assertEquals(bookItem.subtitle, response.getOrThrow().subtitle)
            assertEquals(bookItem.desc, response.getOrThrow().desc)
            assertEquals(bookItem.image, response.getOrThrow().image)
            assertEquals(bookItem.url, response.getOrThrow().url)
            assertEquals(bookItem.year, response.getOrThrow().year)
            assertEquals(bookItem.rating, response.getOrThrow().rating)
            assertEquals(bookItem.priceValue, response.getOrThrow().price)
            assertEquals(bookItem.pages, response.getOrThrow().pages)
            assertEquals(bookItem.language, response.getOrThrow().language)
            assertEquals(bookItem.authors, response.getOrThrow().authors)
            assertEquals(bookItem.publisher, response.getOrThrow().publisher)
            assertFalse(response.getOrThrow().saved!!)
            assertTrue(response.getOrThrow().isFree!!)
            assertEquals(bookItem.pdf!!.freeEBook!!, response.getOrThrow().freePdfUrl!!)
        }
    }

    @Test
    fun `Should return free book detail from remote data source using selected isbn`() {
        runBlocking {
            val bookItem = RemoteData.freeBookApiResponse
            `when`(localDataSource.findSingle(anyString())).thenReturn(flowOf(emptyList()))
            `when`(remoteDataSource.findBook(anyString())).thenReturn(
                flowOf(
                    Result.success(
                        RemoteData.freeBookApiResponse
                    )
                )
            )
            val response = repository.findBook(bookItem.isbn13!!).first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(bookItem.isbn10, response.getOrThrow().isbn10)
            assertEquals(bookItem.isbn13, response.getOrThrow().isbn13)
            assertEquals(bookItem.title, response.getOrThrow().title)
            assertEquals(bookItem.subtitle, response.getOrThrow().subtitle)
            assertEquals(bookItem.desc, response.getOrThrow().desc)
            assertEquals(bookItem.image, response.getOrThrow().image)
            assertEquals(bookItem.url, response.getOrThrow().url)
            assertEquals(bookItem.year, response.getOrThrow().year)
            assertEquals(bookItem.rating, response.getOrThrow().rating)
            assertEquals(bookItem.priceValue, response.getOrThrow().price)
            assertEquals(bookItem.pages, response.getOrThrow().pages)
            assertEquals(bookItem.language, response.getOrThrow().language)
            assertEquals(bookItem.authors, response.getOrThrow().authors)
            assertEquals(bookItem.publisher, response.getOrThrow().publisher)
            assertFalse(response.getOrThrow().saved!!)
            assertTrue(response.getOrThrow().isFree!!)
            assertEquals(bookItem.pdf!!.freeEBook!!, response.getOrThrow().freePdfUrl!!)
        }
    }

    @Test
    fun `Should return purchasable book detail from local data source using selected isbn`() {
        runBlocking {
            val bookItem = RemoteData.purchasableBookApiResponse
            `when`(localDataSource.findSingle(anyString()))
                .thenReturn(flowOf(listOf(toBookDetailEntity(bookItem))))
            val response = repository.findBook(bookItem.isbn13!!).first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(bookItem.isbn10, response.getOrThrow().isbn10)
            assertEquals(bookItem.isbn13, response.getOrThrow().isbn13)
            assertEquals(bookItem.title, response.getOrThrow().title)
            assertEquals(bookItem.subtitle, response.getOrThrow().subtitle)
            assertEquals(bookItem.desc, response.getOrThrow().desc)
            assertEquals(bookItem.image, response.getOrThrow().image)
            assertEquals(bookItem.url, response.getOrThrow().url)
            assertEquals(bookItem.year, response.getOrThrow().year)
            assertEquals(bookItem.rating, response.getOrThrow().rating)
            assertEquals(bookItem.priceValue, response.getOrThrow().price)
            assertEquals(bookItem.pages, response.getOrThrow().pages)
            assertEquals(bookItem.language, response.getOrThrow().language)
            assertEquals(bookItem.authors, response.getOrThrow().authors)
            assertEquals(bookItem.publisher, response.getOrThrow().publisher)
            assertFalse(response.getOrThrow().saved!!)
            assertNull(response.getOrThrow().isFree)
            assertNull(response.getOrThrow().freePdfUrl)
        }
    }

    @Test
    fun `Should return purchasable book detail from remote data source using selected isbn`() {
        runBlocking {
            val bookItem = RemoteData.purchasableBookApiResponse
            `when`(localDataSource.findSingle(anyString())).thenReturn(flowOf(emptyList()))
            `when`(remoteDataSource.findBook(anyString())).thenReturn(flowOf(Result.success(bookItem)))
            val response = repository.findBook(bookItem.isbn13!!).first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(bookItem.isbn10, response.getOrThrow().isbn10)
            assertEquals(bookItem.isbn13, response.getOrThrow().isbn13)
            assertEquals(bookItem.title, response.getOrThrow().title)
            assertEquals(bookItem.subtitle, response.getOrThrow().subtitle)
            assertEquals(bookItem.desc, response.getOrThrow().desc)
            assertEquals(bookItem.image, response.getOrThrow().image)
            assertEquals(bookItem.url, response.getOrThrow().url)
            assertEquals(bookItem.year, response.getOrThrow().year)
            assertEquals(bookItem.rating, response.getOrThrow().rating)
            assertEquals(bookItem.priceValue, response.getOrThrow().price)
            assertEquals(bookItem.pages, response.getOrThrow().pages)
            assertEquals(bookItem.language, response.getOrThrow().language)
            assertEquals(bookItem.authors, response.getOrThrow().authors)
            assertEquals(bookItem.publisher, response.getOrThrow().publisher)
            assertFalse(response.getOrThrow().saved!!)
            assertNull(response.getOrThrow().isFree)
            assertNull(response.getOrThrow().freePdfUrl)
        }
    }
}