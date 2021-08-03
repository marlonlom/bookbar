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

import dev.marlonlom.apps.bookbar.detail.BookDetailsContract.LocalDataSource
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetailsDao
import dev.marlonlom.apps.bookbar.model.network.BookDetailApiResponse
import dev.marlonlom.apps.bookbar.utils.RemoteData
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

fun toBookDetailEntity(item: BookDetailApiResponse) = BookDetail(
    id = item.isbn13!!.substring(3).toInt(),
    isbn13 = item.isbn13!!, isbn10 = item.isbn10!!, title = item.title!!,
    subtitle = item.subtitle!!, rating = item.rating!!, price = item.priceValue!!,
    language = item.language!!, pages = item.pages!!, publisher = item.publisher!!,
    year = item.year!!, authors = item.authors!!, desc = item.desc!!,
    image = item.image!!, url = item.url!!, saved = false,
    isFree = item.pdf?.hasFreeEBook, freePdfUrl = item.pdf?.freeEBook
)

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
    fun `Should save book detail state for selected isbn`() {
        runBlockingTest {
            dataSource.toggleSaved("1001622115721", true)
        }
    }

    @Test
    fun `Should unsave book detail state for selected isbn`() {
        runBlockingTest {
            dataSource.toggleSaved("1001622115721", false)
        }
    }

    @Test
    fun `Should not return book detail using selected isbn`() {
        runBlockingTest {
            `when`(bookDetailsDao.findByIsbn(anyString())).thenReturn(flowOf(emptyList()))
            val response = dataSource.findSingle("9781484266835").first()
            assertNotNull(response)
            assertTrue(response.isNullOrEmpty())
        }
    }

    @Test
    fun `Should return free book detail using selected isbn`() {
        runBlockingTest {
            val bookItem = toBookDetailEntity(RemoteData.freeBookApiResponse)
            val freeBookFlow = flowOf(listOf(bookItem))
            `when`(bookDetailsDao.findByIsbn(anyString())).thenReturn(freeBookFlow)
            val response = dataSource.findSingle(bookItem.isbn13).first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(1, response.size)
            assertEquals(bookItem.id, response[0].id)
            assertEquals(bookItem.isbn10, response[0].isbn10)
            assertEquals(bookItem.isbn13, response[0].isbn13)
            assertEquals(bookItem.title, response[0].title)
            assertEquals(bookItem.subtitle, response[0].subtitle)
            assertEquals(bookItem.image, response[0].image)
            assertEquals(bookItem.desc, response[0].desc)
            assertEquals(bookItem.price, response[0].price)
            assertEquals(bookItem.authors, response[0].authors)
            assertEquals(bookItem.pages, response[0].pages)
            assertEquals(bookItem.publisher, response[0].publisher)
            assertEquals(bookItem.language, response[0].language)
            assertFalse(response[0].saved!!)
            assertTrue(response[0].isFree!!)
            assertEquals(bookItem.freePdfUrl!!, response[0].freePdfUrl!!)

        }
    }

    @Test
    fun `Should return purchasable book detail using selected isbn`() {
        runBlockingTest {
            val bookItem = toBookDetailEntity(RemoteData.purchasableBookApiResponse)
            val purchasableBookFlow = flowOf(listOf(bookItem))
            `when`(bookDetailsDao.findByIsbn(anyString())).thenReturn(purchasableBookFlow)
            val response = dataSource.findSingle(bookItem.isbn13).first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(1, response.size)
            assertEquals(bookItem.id, response[0].id)
            assertEquals(bookItem.isbn10, response[0].isbn10)
            assertEquals(bookItem.isbn13, response[0].isbn13)
            assertEquals(bookItem.title, response[0].title)
            assertEquals(bookItem.subtitle, response[0].subtitle)
            assertEquals(bookItem.image, response[0].image)
            assertEquals(bookItem.desc, response[0].desc)
            assertEquals(bookItem.price, response[0].price)
            assertEquals(bookItem.authors, response[0].authors)
            assertEquals(bookItem.pages, response[0].pages)
            assertEquals(bookItem.publisher, response[0].publisher)
            assertEquals(bookItem.language, response[0].language)
            assertFalse(response[0].saved!!)
            assertNull(response[0].isFree)
            assertNull(response[0].freePdfUrl)

        }
    }

}
