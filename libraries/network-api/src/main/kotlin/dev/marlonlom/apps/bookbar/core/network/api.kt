/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType

/**
 * Bookstore api service interface definition.
 *
 * @author marlonlom
 */
interface BookStoreApiService {

  /**
   * Returns new books from network.
   *
   * @return New books api response.
   */
  suspend fun getNewBooks(): NewBooksApiResponse

  /**
   * Returns book details from network using selected isbn13.
   *
   * @param isbn13 Book isbn13.
   *
   * @return Book details api response.
   */
  suspend fun getBook(isbn13: String): BookDetailsApiResponse
}

/**
 * Bookstore api service default interface implementation.
 *
 * @author marlonlom
 *
 * @property baseUrl Base api url.
 * @property httpClient Http client.
 */
class BookStoreApiServiceImpl(
  private val baseUrl: String,
  private val httpClient: HttpClient = ktorHttpClient,
) : BookStoreApiService {

  override suspend fun getNewBooks(): NewBooksApiResponse = try {
    httpClient.get("${baseUrl}/new") {
      accept(ContentType.Application.Json)
    }.body<NewBooksApiResponse>()
  } catch (exception: Exception) {
    NewBooksApiResponse("New books not available", "0")
  }

  override suspend fun getBook(isbn13: String): BookDetailsApiResponse = try {
    httpClient.get("${baseUrl}/books/${isbn13}") {
      accept(ContentType.Application.Json)
    }.body<BookDetailsApiResponse>()
  } catch (exception: Exception) {
    BookDetailsApiResponse("[books] Not found")
  }

}
