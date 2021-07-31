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

@file:Suppress("SameParameterValue")

package dev.marlonlom.apps.bookbar.utils

import dev.marlonlom.apps.bookbar.model.network.BooksListApiResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object RemoteData {

    private fun readJsonFileText(filePath: String): String =
        this::class.java.classLoader!!.getResource(filePath).readText()

    /*
    val singleBook: String
        get() = readJsonFileText("json/single_book_detail.json")

    val singleFreeBook: String
        get() = readJsonFileText("json/free_book_detail.json")

    val samplePagesFreeBook: String
        get() = readJsonFileText("json/sample_pages_book_detail.json")

    val bookSearchFailed: String
        get() = readJsonFileText("json/book_search_failed.json")

    val bookSearchKotlinPage01: String
        get() = readJsonFileText("json/book_search_kotlin_page01.json")

    val bookSearchKotlinPage03: String
        get() = readJsonFileText("json/book_search_kotlin_page03.json")

    val bookSearchSpanishPage01: String
        get() = readJsonFileText("json/book_search_spanish_page01.json")

     */

    val releasedBooksApiResponse: BooksListApiResponse
        get() = Json.decodeFromString(readJsonFileText("json/new_books.json"))

}