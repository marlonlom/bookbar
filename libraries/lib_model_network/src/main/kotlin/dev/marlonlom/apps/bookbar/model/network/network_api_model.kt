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

import kotlinx.serialization.Serializable

/**
 * Data class that represents book list api response.
 *
 * @author marlonlom
 */
@Serializable
data class BooksListApiResponse(
    val books: List<BookListItem>,
    val error: String,
    var total: String
)

/**
 * Data class that represents book list item from a list.
 *
 * @author marlonlom
 */
@Serializable
data class BookListItem(
    val image: String,
    val isbn13: String,
    val price: String,
    val subtitle: String,
    val title: String,
    val url: String
) {

    /**
     * Returns if price is greater than zero, the price value, instead, 'free'.
     * @return the price value, or 'free'
     */
    val priceValue
        get() = if (price == "\$0.00") "free" else price

    /**
     * Return true/false if contents of two instances of book list item class are the same.
     *
     * @param anotherItem another instance of book list item class
     *
     * @return true/false
     */
    fun contentEquals(anotherItem: BookListItem): Boolean =
        (this.isbn13 == anotherItem.isbn13 && this.title == anotherItem.title
                && this.subtitle == anotherItem.subtitle && this.image == anotherItem.image
                && this.url == anotherItem.url && this.price == anotherItem.price)
}

/**
 * Data class that represents api response for book details searched using the ISBN.
 *
 * @author marlonlom
 */
@Serializable
data class BookDetailApiResponse(
    val authors: String? = null,
    val desc: String? = null,
    val error: String,
    val image: String? = null,
    val isbn10: String? = null,
    val isbn13: String? = null,
    val language: String? = null,
    val pages: String? = null,
    val price: String? = null,
    val publisher: String? = null,
    val rating: String? = null,
    val subtitle: String? = null,
    val title: String? = null,
    val url: String? = null,
    val year: String? = null
) {
    /**
     * Returns if price is greater than zero, the price value, instead, 'free'.
     * @return the price value, or 'free'
     */
    val priceValue
        get() = if (price == "\$0.00") "free" else price

}