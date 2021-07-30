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
class SavedBooksDaoTest : CommonDaoTest() {

    @Test
    fun testSuccessSavedBooksListIsEmpty() {
        coroutineTestRule.testScope.launch {
            val list = db.bookDetailsDao().listSaved().first()
            assertTrue(list.isEmpty())
        }
    }

    @Test
    fun testSuccessSavedBooksListIsNotEmptyAfterInsertingData() {
        coroutineTestRule.testScope.launch {
            val item = SampleData.singleBookDetail
            with(db.bookDetailsDao()) {
                insert(item)
                toggleSaved(item.isbn13, true)
            }
            val list = db.bookDetailsDao().listSaved().first()
            assertTrue(list.isNotEmpty())
            assertEquals(list.size, list.size)
        }
    }

    @Test
    fun testFailedSavedBooksListIsEmptyAfterInsertingData() {
        coroutineTestRule.testScope.launch {
            val item = SampleData.singleBookDetail
            db.bookDetailsDao().insert(item)
            val list = db.bookDetailsDao().listSaved().first()
            assertTrue(list.isNullOrEmpty())
        }
    }

    @Test
    fun testSuccessBookSearchContainsSearchedTerm() {
        coroutineTestRule.testScope.launch {
            val item = SampleData.singleBookDetail
            with(db.bookDetailsDao()) {
                insert(item)
                toggleSaved(item.isbn13, true)
            }
            val list = db.bookDetailsDao().searchSaved("*learning kotlin*").first()
            assertTrue(list.isNotEmpty())
            assertEquals(item.title, list[0].title)
            assertEquals(item.isbn13, list[0].isbn13)
        }
    }

    @Test
    fun testFailedBookSearchNotContainsSearchedTerm() {
        coroutineTestRule.testScope.launch {
            val item = SampleData.singleBookDetail
            with(db.bookDetailsDao()) {
                insert(item)
                toggleSaved(item.isbn13, true)
            }
            val list = db.bookDetailsDao().searchSaved("*learning xpath*").first()
            assertTrue(list.isNullOrEmpty())
        }
    }
}