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

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.IOException

@ExperimentalCoroutinesApi
abstract class CommonDaoTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    protected lateinit var db: AppDatabase

    @Before
    fun createDatabase() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        val executor = coroutineTestRule.testDispatcher.asExecutor()
        db = Room.inMemoryDatabaseBuilder(applicationContext, AppDatabase::class.java)
            .setTransactionExecutor(executor)
            .setQueryExecutor(executor)
            .allowMainThreadQueries().build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}
