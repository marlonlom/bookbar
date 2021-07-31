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

package dev.marlonlom.apps.bookbar.categories

import dev.marlonlom.apps.bookbar.utils.RemoteData.bookCategoriesList
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListAdapterDiffCallbackTest : TestCase() {

    private val diffCallback = BookCategoriesListAdapter.DiffCallback()

    @Test
    fun `Should return false when comparing the contents of two items`() {
        val oldItem = bookCategoriesList[0]
        val newItem = bookCategoriesList[1]
        assertFalse(diffCallback.areItemsTheSame(oldItem, newItem))
        assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
    }

    @Test
    fun `Should return false when comparing the contents of two item but same id`() {
        val oldItem = bookCategoriesList[0]
        val newItem = oldItem.copy(title = "Random")
        assertTrue(diffCallback.areItemsTheSame(oldItem, newItem))
        assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
    }

    @Test
    fun `Should return true when comparing the same item`() {
        val oldItem = bookCategoriesList[0]
        val newItem = oldItem.copy()
        assertTrue(diffCallback.areItemsTheSame(oldItem, newItem))
        assertTrue(diffCallback.areContentsTheSame(oldItem, newItem))
    }
}