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
class BookCategoriesDaoTest : CommonDaoTest() {

    @Test
    fun testSuccessCategoriesListIsEmpty() {
        coroutineTestRule.testScope.launch {
            val list = db.categoriesDao().listAll().first()
            assertTrue(list.isEmpty())
        }
    }

    @Test
    fun testSuccessCategoriesListIsNotEmptyAfterInsertingData() {
        coroutineTestRule.testScope.launch {
            val items = SampleData.bookCategoriesList
            db.categoriesDao().insertCategories(items)
            val singleStats = db.categoriesDao().listAll().first()
            assertTrue(singleStats.isNotEmpty())
            assertEquals(items.size, singleStats.size)
        }
    }

    @Test
    fun testSuccessCategoriesListContainsSearchedTerm() {
        coroutineTestRule.testScope.launch {
            val items = SampleData.bookCategoriesList
            db.categoriesDao().insertCategories(items)
            val list = db.categoriesDao().search("*android*").first()
            assertTrue(list.isNotEmpty())
            assertEquals("android", list[0].tag)
            assertEquals("Android", list[0].title)
        }
    }

    @Test
    fun testFailedCategoriesListNotContainsSearchedTerm() {
        coroutineTestRule.testScope.launch {
            val items = SampleData.bookCategoriesList
            db.categoriesDao().insertCategories(items)
            val list = db.categoriesDao().search("*spanish*").first()
            assertTrue(list.isNullOrEmpty())
        }
    }

    @Test
    fun testSuccessCategoriesListIsEmptyAfterDeletingData() {
        coroutineTestRule.testScope.launch {
            val items = SampleData.bookCategoriesList
            with(db.categoriesDao()) {
                insertCategories(items)
                deleteAll()
            }
            val list = db.categoriesDao().listAll().first()
            assertTrue(list.isNotEmpty())
            assertEquals(items.size, list.size)
        }
    }
}