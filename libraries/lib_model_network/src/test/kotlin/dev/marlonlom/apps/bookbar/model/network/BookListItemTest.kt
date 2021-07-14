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

import dev.marlonlom.apps.bookbar.model.network.utils.RemoteData
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BookListItemTest : TestCase() {

    @Test
    fun `Should return true comparing two book list item contents`() {
        val firstItem = RemoteData.newBooksApiResponse.books[0]
        val secondItem = RemoteData.newBooksApiResponse.books[0]
        assertTrue(firstItem.contentEquals(secondItem))
    }

    @Test
    fun `Should return false comparing two book list item contents`() {
        val firstItem = RemoteData.newBooksApiResponse.books[0]
        val secondItem = RemoteData.newBooksApiResponse.books[1]
        assertFalse(firstItem.contentEquals(secondItem))
    }

    @Test
    fun `Should return free if book price is zero`() {
        val firstItem = RemoteData.newBooksApiResponse.books[0]
        assertEquals("Free", firstItem.priceValue)
    }

    @Test
    fun `Should return regular price if book price is greater than zero`() {
        val firstItem = RemoteData.newBooksApiResponse.books[7]
        assertEquals("\$16.83", firstItem.priceValue)
    }

}