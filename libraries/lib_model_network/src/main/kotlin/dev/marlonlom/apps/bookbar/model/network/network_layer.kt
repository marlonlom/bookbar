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

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Api definition interface for Book store service.
 *
 * @author marlonlom
 */
interface BookStoreApi {

    /**
     * Return list of new books from api.
     *
     * @return api response with list of new books
     */
    @GET("new")
    suspend fun getNewBooks(): BooksListApiResponse

    @GET("books/{isbn13}")
    suspend fun getBookDetail(@Path("isbn13") isbn: String): BookDetailApiResponse

    @GET("search/{query}/{page}")
    suspend fun search(
        @Path("query") query: String,
        @Path("page") page: String? = "1"
    ): BookSearchApiResponse

    @ExperimentalSerializationApi
    object Service {
        private val json = Json { ignoreUnknownKeys = true }

        fun newService(httpUrl: HttpUrl): BookStoreApi {
            val contentType = "application/json".toMediaType()

            return Retrofit.Builder()
                .addConverterFactory(json.asConverterFactory(contentType))
                .baseUrl(httpUrl)
                .build().create(BookStoreApi::class.java)
        }
    }

}