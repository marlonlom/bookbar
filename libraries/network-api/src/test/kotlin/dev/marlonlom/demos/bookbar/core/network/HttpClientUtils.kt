/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json

object HttpClientUtils {

  fun useMockEngine(jsonPath: String) = MockEngine { _ ->
    val readText: String = this.javaClass.classLoader!!.getResource(jsonPath)!!.readText()
    respond(
      content = ByteReadChannel(readText),
      status = HttpStatusCode.OK,
      headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
  }

  fun useHttpClient(mockEngine: MockEngine) = HttpClient(mockEngine) {
    install(ContentNegotiation) {
      json(Json {
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
        ignoreUnknownKeys = true
      })
    }
  }
}
