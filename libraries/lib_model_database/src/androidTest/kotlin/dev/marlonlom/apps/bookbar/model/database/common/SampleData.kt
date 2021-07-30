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

package dev.marlonlom.apps.bookbar.model.database.common

import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.model.database.categories.BookCategory
import dev.marlonlom.apps.bookbar.model.database.released_books.ReleasedBook
import org.json.JSONArray
import org.json.JSONObject

object SampleData {

    private fun readJsonFileText(filePath: String): String =
        this::class.java.classLoader!!.getResource(filePath).readText()

    val bookCategoriesList: List<BookCategory>
        get() = readJsonFileText("json/book_categories.json").let {
            val jsonArray = JSONArray(it)
            val result = mutableListOf<BookCategory>()
            for (pos in 0 until jsonArray.length()) {
                result.add(
                    jsonArray.getJSONObject(pos).let { jsonObject ->
                        BookCategory(
                            id = pos + 1,
                            tag = jsonObject.getString("tag"),
                            title = jsonObject.getString("title")
                        )
                    })
            }
            result
        }

    val releasedBooksList: List<ReleasedBook>
        get() = readJsonFileText("json/released_books.json").let {
            val jsonArray = JSONArray(it)
            val result = mutableListOf<ReleasedBook>()
            for (pos in 0 until jsonArray.length()) {
                result.add(
                    jsonArray.getJSONObject(pos).let { jsonObject ->
                        ReleasedBook(
                            isbn13 = jsonObject.getString("isbn13"),
                            title = jsonObject.getString("title"),
                            subtitle = jsonObject.getString("subtitle"),
                            price = jsonObject.getString("price"),
                            image = jsonObject.getString("image"),
                            url = jsonObject.getString("url")
                        )
                    })
            }
            result
        }

    val singleBookDetail: BookDetail
        get() = readJsonFileText("json/book_details.json").let {
            val jsonObject = JSONObject(it)
            val result = BookDetail(
                id = jsonObject.getString("isbn10").toInt(),
                isbn13 = jsonObject.getString("isbn13"),
                isbn10 = jsonObject.getString("isbn10"),
                title = jsonObject.getString("title"),
                subtitle = jsonObject.getString("subtitle"),
                rating = jsonObject.getString("rating"),
                price = jsonObject.getString("price"),
                language = jsonObject.getString("language"),
                pages = jsonObject.getString("pages"),
                publisher = jsonObject.getString("publisher"),
                year = jsonObject.getString("year"),
                authors = jsonObject.getString("authors"),
                desc = jsonObject.getString("desc"),
                image = jsonObject.getString("image"),
                url = jsonObject.getString("url")
            )
            result
        }

}