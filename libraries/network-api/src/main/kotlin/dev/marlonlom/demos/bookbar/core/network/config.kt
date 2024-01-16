/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.core.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIME_OUT = 60_000

/**
 * Ktor private http client implementation.
 *
 * @author marlonlom
 */
internal val ktorHttpClient = HttpClient(Android) {

  install(ContentNegotiation) {
    json(Json {
      prettyPrint = true
      isLenient = true
      encodeDefaults = true
      ignoreUnknownKeys = true
    })

    engine {
      connectTimeout = TIME_OUT
      socketTimeout = TIME_OUT
    }
  }

  Logging {
    logger = object : Logger {
      override fun log(message: String) {
        Log.d("Ktor.Logger", message)
      }
    }
    level = LogLevel.INFO
  }

  ResponseObserver { response ->
    Log.d("Ktor.HTTP_status:", "${response.status.value}")
  }

  install(DefaultRequest) {
    contentType(ContentType.Application.Json)
  }
}
