/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.network

import dev.marlonlom.apps.bookbar.core.network.HttpClientUtils.useHttpClient
import dev.marlonlom.apps.bookbar.core.network.HttpClientUtils.useMockEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

internal class NewBooksApiServiceTest {

  @Test
  fun `Should successfully fetch new books`(): Unit = runBlocking {
    val httpClient = useHttpClient(
      useMockEngine("books-new/success.json")
    )
    val apiClient: BookStoreApiService = BookStoreApiServiceImpl("baseUrl", httpClient)
    val apiResponse = apiClient.getNewBooks()

    assertNotNull(apiResponse)
    assertEquals("0", apiResponse.error)
    assertEquals("20", apiResponse.total)
    assertTrue(apiResponse.books.isNotEmpty())
    with(apiResponse.books[0]) {
      assertEquals("An Introduction to C & GUI Programming, 2nd Edition", this.title)
      assertEquals("", this.subtitle)
      assertEquals("9781912047451", this.isbn13)
      assertEquals("$14.92", this.price)
      assertEquals("https://itbook.store/img/books/9781912047451.png", this.image)
      assertEquals("https://itbook.store/books/9781912047451", this.url)
    }
  }

  @Test
  fun `Should fetch new books failure response`(): Unit = runBlocking {
    val httpClient = useHttpClient(
      useMockEngine("books-new/failure.json")
    )
    val apiClient: BookStoreApiService = BookStoreApiServiceImpl("baseUrl", httpClient)
    val apiResponse = apiClient.getNewBooks()

    assertNotNull(apiResponse)
    assertEquals("New books not available", apiResponse.error)
    assertEquals("0", apiResponse.total)
    assertFalse(apiResponse.books.isNotEmpty())
  }

}
