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

import dev.marlonlom.apps.bookbar.categories.BrowseCategoriesContract.LocalDataSource
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.categories.CategoriesDao
import dev.marlonlom.apps.bookbar.utils.RemoteData.bookCategoriesList
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceTest : TestCase() {

    private lateinit var dataSource: LocalDataSource
    private lateinit var database: AppDatabase
    private lateinit var categoriesDao: CategoriesDao

    @Before
    public override fun setUp() {
        database = mock(AppDatabase::class.java)
        categoriesDao = mock(CategoriesDao::class.java)
        dataSource = LocalDataSource(database)
        `when`(database.categoriesDao()).thenReturn(categoriesDao)
    }

    @Test
    fun `Should return empty list of categories`() {
        runBlocking {
            `when`(categoriesDao.listAll()).thenReturn(flowOf(emptyList()))
            val dataSourceResponse = dataSource.listAll().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isNullOrEmpty())
        }
    }

    @Test
    fun `Should return a list of categories`() {
        runBlocking {
            val resultsFlow = flowOf(bookCategoriesList)
            `when`(categoriesDao.listAll()).thenReturn(resultsFlow)
            val dataSourceResponse = dataSource.listAll().first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isNotEmpty())
            assertEquals(bookCategoriesList.size, dataSourceResponse.size)
        }
    }

    @Test
    fun `Should find a category using search`() {
        runBlocking {
            val resultsFlow = flowOf(bookCategoriesList.filter { it.title == "Android" })
            `when`(categoriesDao.search(anyString())).thenReturn(resultsFlow)
            val dataSourceResponse = dataSource.search("*android*").first()
            assertNotNull(dataSourceResponse)
            assertTrue(dataSourceResponse.isNotEmpty())
            assertEquals(1, dataSourceResponse.size)
        }
    }

    @Test
    fun `Should not find a category using search`() {
        runBlocking {
            val resultsFlow = flowOf(bookCategoriesList.filter { it.title == "Llorem ipsum" })
            `when`(categoriesDao.search(anyString())).thenReturn(resultsFlow)
            val dataSourceResponse = dataSource.search("*lorem ipsum*").first()
            assertTrue(dataSourceResponse.isNullOrEmpty())
        }
    }

    @Test
    fun `Should delete a previously inserted category`() {
        runBlocking {
            `when`(categoriesDao.listAll()).thenReturn(flowOf(emptyList()))
            with(dataSource) {
                insertCategories(bookCategoriesList)
                deleteAll()
            }
            val dataSourceResponse = dataSource.listAll().first()
            assertTrue(dataSourceResponse.isNullOrEmpty())
        }
    }
}
