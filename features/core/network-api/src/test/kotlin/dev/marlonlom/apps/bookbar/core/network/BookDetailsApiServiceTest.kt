/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.network

import dev.marlonlom.apps.bookbar.core.network.HttpClientUtils.useHttpClient
import dev.marlonlom.apps.bookbar.core.network.HttpClientUtils.useMockEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

internal class BookDetailsApiServiceTest {

  @Test
  fun `Should successfully fetch book details`(): Unit = runBlocking {
    val httpClient = useHttpClient(
      useMockEngine("book-detail/success.json")
    )
    val apiClient: BookStoreApiService = BookStoreApiServiceImpl("baseUrl", httpClient)
    val apiResponse = apiClient.getBook("9781098106225")

    assertNotNull(apiResponse)
    assertEquals("0", apiResponse.error)
    assertNotNull(apiResponse.title)
    assertNotNull(apiResponse.subtitle)
    assertNotNull(apiResponse.authors)
    assertNotNull(apiResponse.publisher)
    assertNotNull(apiResponse.language)
    assertNotNull(apiResponse.isbn10)
    assertNotNull(apiResponse.isbn13)
    assertNotNull(apiResponse.pages)
    assertNotNull(apiResponse.year)
    assertNotNull(apiResponse.rating)
    assertNotNull(apiResponse.desc)
    assertNotNull(apiResponse.price)
    assertNotNull(apiResponse.image)
    assertNotNull(apiResponse.url)
  }

  @Test
  fun `Should fetch new books failure response`(): Unit = runBlocking {
    val httpClient = useHttpClient(
      useMockEngine("book-detail/failure.json")
    )
    val apiClient: BookStoreApiService = BookStoreApiServiceImpl("baseUrl", httpClient)
    val apiResponse = apiClient.getBook("9781098106225")

    assertNotNull(apiResponse)
    assertEquals("[books] Not found", apiResponse.error)
    assertNull(apiResponse.title)
    assertNull(apiResponse.subtitle)
    assertNull(apiResponse.authors)
    assertNull(apiResponse.publisher)
    assertNull(apiResponse.language)
    assertNull(apiResponse.isbn10)
    assertNull(apiResponse.isbn13)
    assertNull(apiResponse.pages)
    assertNull(apiResponse.year)
    assertNull(apiResponse.rating)
    assertNull(apiResponse.desc)
    assertNull(apiResponse.price)
    assertNull(apiResponse.image)
    assertNull(apiResponse.url)
  }

  @Test
  fun `Should fetch new books failure response for another error`(): Unit = runBlocking {
    val httpClient = useHttpClient(
      useMockEngine("books-new/failure.json")
    )
    val apiClient: BookStoreApiService = BookStoreApiServiceImpl("baseUrl", httpClient)
    val apiResponse = apiClient.getBook("9781098106225")

    assertNotNull(apiResponse)
    assertEquals("[books] Not found", apiResponse.error)
    assertNull(apiResponse.title)
    assertNull(apiResponse.subtitle)
    assertNull(apiResponse.authors)
    assertNull(apiResponse.publisher)
    assertNull(apiResponse.language)
    assertNull(apiResponse.isbn10)
    assertNull(apiResponse.isbn13)
    assertNull(apiResponse.pages)
    assertNull(apiResponse.year)
    assertNull(apiResponse.rating)
    assertNull(apiResponse.desc)
    assertNull(apiResponse.price)
    assertNull(apiResponse.image)
    assertNull(apiResponse.url)
  }

}
