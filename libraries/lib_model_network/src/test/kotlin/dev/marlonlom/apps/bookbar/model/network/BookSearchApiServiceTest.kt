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
class BookSearchApiServiceTest : TestCase() {

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
    fun `Should return error when searching book using invalid query text`() {
        runBlocking {
            mockApiService.search("").apply {
                assertNotNull(this)
                assertEquals("[search] Invalid request", this.error)
            }
        }
    }

    @Test
    fun `Should return first page of books list when searching book using text 'kotlin'`() {
        runBlocking {
            mockApiService.search("kotlin").apply {
                assertNotNull(this)
                assertEquals("0", this.error)
                assertEquals("16", this.total)
                assertEquals("1", this.page)
                assertTrue(this.books!!.isNotEmpty())
            }
        }
    }

    @Test
    fun `Should return empty result for third page of books list when searching book using text 'kotlin'`() {
        runBlocking {
            mockApiService.search("kotlin", "3").apply {
                assertNotNull(this)
                assertEquals("0", this.error)
                assertEquals("0", this.total)
                assertEquals("3", this.page)
                assertTrue(this.books!!.isNullOrEmpty())
            }
        }
    }

    @Test
    fun `Should return empty result for first page of books list when searching book using text 'spanish'`() {
        runBlocking {
            mockApiService.search("spanish").apply {
                assertNotNull(this)
                val emptyResponse = BookSearchApiResponse.EMPTY_RESPONSE
                assertEquals(emptyResponse.error, this.error)
                assertEquals(emptyResponse.total, this.total)
                assertEquals(emptyResponse.page, this.page)
                assertEquals(emptyResponse.books.isNullOrEmpty(), this.books!!.isNullOrEmpty())
            }
        }
    }

}
