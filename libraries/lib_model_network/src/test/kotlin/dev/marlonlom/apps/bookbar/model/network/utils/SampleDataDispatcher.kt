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

package dev.marlonlom.apps.bookbar.model.network.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection

object SampleDataDispatcher {

    private val newBooksJson = RemoteData.newBooks
    private val singleBookJson = RemoteData.singleBook
    private val singleBookNotFoundJson = RemoteData.singleBookNotFound
    private const val errorJson = "{\"error\": \"[api] Not found\"}"

    private fun newMockResponse(jsonString: String) = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK).setBody(jsonString)

    val dispatcher = object : Dispatcher() {

        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            println("requested path: ${request.path}")
            return when (request.path) {
                "/new" -> newMockResponse(newBooksJson)
                "/books/9781484266823" -> newMockResponse(singleBookJson)
                "/books/0000000000005" -> newMockResponse(singleBookNotFoundJson)
                else -> newMockResponse(errorJson)
            }
        }

    }

}