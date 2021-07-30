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

package dev.marlonlom.apps.bookbar.model.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.marlonlom.apps.bookbar.model.database.common.CommonDaoTest
import dev.marlonlom.apps.bookbar.model.database.common.SampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ReleasedBooksDaoTest : CommonDaoTest() {

    @Test
    fun testSuccessReleasedBooksListIsEmpty() {
        coroutineTestRule.testScope.launch {
            val list = db.releasedBooksDao().listAll().first()
            assertTrue(list.isNullOrEmpty())
        }
    }

    @Test
    fun testSuccessReleasedBooksListIsNotEmptyAfterInsertingData() {
        coroutineTestRule.testScope.launch {
            val items = SampleData.releasedBooksList
            items.forEach { item -> db.releasedBooksDao().insert(item) }
            val list = db.releasedBooksDao().listAll().first()
            assertTrue(list.isNotEmpty())
            assertEquals(items.size, list.size)
        }
    }

    @Test
    fun testSuccessReleasedBookFoundByIsbnSearch() {
        coroutineTestRule.testScope.launch {
            val isbnToSearch = "9781119456339"
            val items = SampleData.releasedBooksList
            items.forEach { item -> db.releasedBooksDao().insert(item) }
            val list = db.releasedBooksDao().findByIsbn(isbnToSearch).first()
            assertTrue(list.isNotEmpty())
            assertEquals(isbnToSearch, list[0].isbn13)
            assertEquals("Operating System Concepts, 10th Edition", list[0].title)
            assertEquals("", list[0].subtitle)
            assertEquals("https://itbook.store/img/books/$isbnToSearch.png", list[0].image)
            assertEquals("$90.08", list[0].price)
            assertEquals("https://itbook.store/books/$isbnToSearch", list[0].url)
        }
    }

    @Test
    fun testSuccessReleasedBooksListIsEmptyAfterDeletingData() {
        coroutineTestRule.testScope.launch {
            with(db.releasedBooksDao()) {
                SampleData.releasedBooksList.forEach { item -> insert(item) }
                deleteAll()
            }
            val list = db.releasedBooksDao().listAll().first()
            assertTrue(list.isNullOrEmpty())
        }
    }

}