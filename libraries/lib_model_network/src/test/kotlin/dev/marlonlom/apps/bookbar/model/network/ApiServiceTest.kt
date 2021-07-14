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

package dev.marlonlom.apps.bookbar.model.network

import dev.marlonlom.apps.bookbar.model.network.utils.SampleDataDispatcher
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalSerializationApi
@RunWith(MockitoJUnitRunner::class)
class ApiServiceTest : TestCase() {

    private var mockWebServer = MockWebServer()
    private lateinit var webApi: BookStoreApi.Service
    private lateinit var mockApiService: BookStoreApi

    @Before
    fun setup() {
        webApi = Mockito.spy(BookStoreApi.Service::class.java)
        mockWebServer.apply {
            dispatcher = SampleDataDispatcher.dispatcher
            start()
        }
        mockApiService = webApi.newService(mockWebServer.url("/"))
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should return new books list`() {
        runBlocking {
            mockApiService.getNewBooks().apply {
                assertNotNull(this)
                assertEquals("0", this.error)
                assertEquals("20", this.total)
                assertFalse(this.books.isNullOrEmpty())
                if (this.books.isNotEmpty()) {
                    val bookListItem = this.books[0]
                    assertNotNull(bookListItem)
                    assertEquals("TypeScript Notes for Professionals", bookListItem.title)
                    assertEquals("", bookListItem.subtitle)
                    assertEquals("1001622115721", bookListItem.isbn13)
                    assertEquals("$0.00", bookListItem.price)
                    assertEquals("Free", bookListItem.priceValue)
                    assertEquals(
                        "https://itbook.store/img/books/1001622115721.png",
                        bookListItem.image
                    )
                    assertEquals("https://itbook.store/books/1001622115721", bookListItem.url)
                }
            }
        }
    }

    @Test
    fun `Should return error when searching book using invalid ISBN 0000000000005`() {
        runBlocking {
            mockApiService.getBookDetail("0000000000005").apply {
                assertNotNull(this)
                assertEquals("[books] Not found", this.error)
            }
        }
    }

    @Test
    fun `Should return a book using a ISBN`() {
        runBlocking {
            mockApiService.getBookDetail("9781484266823").apply {
                assertNotNull(this)
                assertEquals("0", this.error)
                assertEquals("Beginning Power Apps", this.title)
                assertEquals(
                    "The Non-Developer's Guide to Building Business Applications",
                    this.subtitle
                )
                assertEquals("Tim Leung", this.authors)
                assertEquals("Apress", this.publisher)
                assertEquals("English", this.language)
                assertEquals("148426682X", this.isbn10)
                assertEquals("9781484266823", this.isbn13)
                assertEquals("943", this.pages)
                assertEquals("2021", this.year)
                assertEquals("0", this.rating)
                assertEquals(
                    "Transform the way your business works with easy-to-build apps. With this updated and expanded second edition, you can build business apps that work with your company's systems and databases, without having to enlist the expertise of costly, professionally trained software developers.In this new edit...",
                    this.desc
                )
                assertEquals("$37.24", this.price)
                assertEquals("$37.24", this.priceValue)
                assertEquals("https://itbook.store/img/books/9781484266823.png", this.image)
                assertEquals("https://itbook.store/books/9781484266823", this.url)
            }
        }
    }

    @Test
    fun `Should return a free book using a ISBN`() {
        runBlocking {
            mockApiService.getBookDetail("1001622115721").apply {
                assertNotNull(this)
                assertEquals("0", this.error)
                assertEquals("TypeScript Notes for Professionals", this.title)
                assertEquals("", this.subtitle)
                assertEquals("Stack Overflow Community", this.authors)
                assertEquals("Self-publishing", this.publisher)
                assertEquals("English", this.language)
                assertEquals("1622115724", this.isbn10)
                assertEquals("1001622115721", this.isbn13)
                assertEquals("96", this.pages)
                assertEquals("2018", this.year)
                assertEquals("0", this.rating)
                assertEquals(
                    "The TypeScript Notes for Professionals book is compiled from Stack Overflow Documentation, the content is written by the beautiful people at Stack Overflow....",
                    this.desc
                )
                assertEquals("$0.00", this.price)
                assertEquals("Free", this.priceValue)
                assertEquals("https://itbook.store/img/books/1001622115721.png", this.image)
                assertEquals("https://itbook.store/books/1001622115721", this.url)
                assertNotNull(this.pdf)
                assertTrue(this.pdf!!.hasFreeEBook)
                assertEquals(
                    "https://www.dbooks.org/d/5592544360-1622115253-9bbc1cd0a894d0c9/",
                    this.pdf!!.freeEBook!!
                )
            }
        }
    }

    @Test
    fun `Should return a free book with sample pages using a ISBN`() {
        runBlocking {
            mockApiService.getBookDetail("9781617294136").apply {
                assertNotNull(this)
                assertEquals("0", this.error)
                assertEquals("Securing DevOps", this.title)
                assertEquals("Security in the Cloud", this.subtitle)
                assertEquals("Julien Vehent", this.authors)
                assertEquals("Manning", this.publisher)
                assertEquals("English", this.language)
                assertEquals("1617294136", this.isbn10)
                assertEquals("9781617294136", this.isbn13)
                assertEquals("384", this.pages)
                assertEquals("2018", this.year)
                assertEquals("5", this.rating)
                assertEquals(
                    "An application running in the cloud can benefit from incredible efficiencies, but they come with unique security threats too. A DevOps team's highest priority is understanding those risks and hardening the system against them.Securing DevOps teaches you the essential techniques to secure your cloud ...",
                    this.desc
                )
                assertEquals("$39.65", this.price)
                assertEquals("$39.65", this.priceValue)
                assertEquals("https://itbook.store/img/books/9781617294136.png", this.image)
                assertEquals("https://itbook.store/books/9781617294136", this.url)
                assertNotNull(this.pdf)
                assertFalse(this.pdf!!.hasFreeEBook)
                assertEquals("none", this.pdf!!.freeEBook!!)
            }
        }
    }

}