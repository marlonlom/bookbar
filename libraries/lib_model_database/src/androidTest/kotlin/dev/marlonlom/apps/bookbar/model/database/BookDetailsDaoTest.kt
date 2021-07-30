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
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class BookDetailsDaoTest : CommonDaoTest() {

    @Test
    fun testSuccessBookDetailNotFoundByIsbn() {
        coroutineTestRule.testScope.launch {
            val list = db.bookDetailsDao().findByIsbn("9783649092803").first()
            Assert.assertTrue(list.isNullOrEmpty())
        }
    }

    @Test
    fun testSuccessBookDeletedAfterInsertingIt() {
        coroutineTestRule.testScope.launch {
            val item = SampleData.singleBookDetail
            with(db.bookDetailsDao()) {
                insert(item)
                delete(item.isbn13)
            }
            val list = db.bookDetailsDao().findByIsbn(item.isbn13).first()
            Assert.assertTrue(list.isNullOrEmpty())
        }
    }

    @Test
    fun testSuccessBookDetailFoundByIsbn() {
        coroutineTestRule.testScope.launch {
            val item = SampleData.singleBookDetail
            db.bookDetailsDao().insert(item)
            val list = db.bookDetailsDao().findByIsbn(item.isbn13).first()
            Assert.assertTrue(list.isNotEmpty())
            Assert.assertEquals(1, list.size)
            Assert.assertEquals(1, list[0].authors)
            Assert.assertEquals(item.id, list[0].id)
            Assert.assertEquals(item.isbn13, list[0].isbn13)
            Assert.assertEquals(item.isbn10, list[0].isbn10)
            Assert.assertEquals(item.title, list[0].title)
            Assert.assertEquals(item.subtitle, list[0].subtitle)
            Assert.assertEquals(item.rating, list[0].rating)
            Assert.assertEquals(item.price, list[0].price)
            Assert.assertEquals(item.language, list[0].language)
            Assert.assertEquals(item.pages, list[0].pages)
            Assert.assertEquals(item.publisher, list[0].publisher)
            Assert.assertEquals(item.year, list[0].year)
            Assert.assertEquals(item.authors, list[0].authors)
            Assert.assertEquals(item.desc, list[0].desc)
            Assert.assertEquals(item.image, list[0].image)
            Assert.assertEquals(item.url, list[0].url)
            Assert.assertFalse(list[0].saved!!)
        }
    }
}