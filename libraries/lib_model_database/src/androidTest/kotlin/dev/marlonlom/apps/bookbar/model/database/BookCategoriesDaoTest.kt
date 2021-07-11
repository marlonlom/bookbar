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

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.marlonlom.apps.bookbar.model.database.common.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class BookCategoriesDaoTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var db: AppDatabase

    @Before
    fun createDatabase() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(applicationContext, AppDatabase::class.java)
            .setTransactionExecutor(coroutineTestRule.testDispatcher.asExecutor())
            .setQueryExecutor(coroutineTestRule.testDispatcher.asExecutor())
            .allowMainThreadQueries().build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testSuccessCategoriesListIsEmpty() {
        coroutineTestRule.testScope.launch {
            val singleStats = db.categoriesDao().listAll().first()
            Assert.assertTrue(singleStats.isEmpty())
        }
    }
}