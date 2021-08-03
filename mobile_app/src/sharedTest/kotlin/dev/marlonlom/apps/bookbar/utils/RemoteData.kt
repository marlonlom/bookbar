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

import dev.marlonlom.apps.bookbar.model.database.categories.BookCategory
import dev.marlonlom.apps.bookbar.model.network.BookDetailApiResponse
import dev.marlonlom.apps.bookbar.model.network.BooksListApiResponse
import dev.marlonlom.apps.bookbar.model.network.SavedBooksListApiResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream

object RemoteData {

    private val json = Json { ignoreUnknownKeys = true }

    private fun readJsonFileText(filePath: String): String =
        this::class.java.classLoader!!.getResource(filePath).readText()

    private fun readJsonFileStream(filePath: String): InputStream =
        this::class.java.classLoader!!.getResourceAsStream(filePath)

    val releasedBooksApiResponse: BooksListApiResponse
        get() = json.decodeFromString(readJsonFileText("json/new_books.json"))

    val freeBookApiResponse: BookDetailApiResponse
        get() = json.decodeFromString(readJsonFileText("json/free_book_detail.json"))

    val purchasableBookApiResponse: BookDetailApiResponse
        get() = json.decodeFromString(readJsonFileText("json/single_book_detail.json"))

    val savedBooksApiResponse: SavedBooksListApiResponse
        get() = json.decodeFromString(readJsonFileText("json/saved_books.json"))

    val bookCategoriesStream: InputStream = readJsonFileStream("json/book_categories.txt")

    val bookCategoriesList: List<BookCategory>
        get() = readJsonFileStream("json/book_categories.txt").let {
            val result = mutableListOf<BookCategory>()
            it.bufferedReader().useLines { lines ->
                lines.forEachIndexed { index, line ->
                    val categoryPart = line.split(";")
                    result.add(
                        BookCategory(
                            id = index + 1,
                            tag = categoryPart[1],
                            title = categoryPart[0]
                        )
                    )
                }
            }
            result
        }

}