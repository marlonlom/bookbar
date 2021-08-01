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
import dev.marlonlom.apps.bookbar.categories.BrowseCategoriesContract.Repository
import dev.marlonlom.apps.bookbar.utils.RemoteData
import dev.marlonlom.apps.bookbar.utils.RemoteData.bookCategoriesList
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest : TestCase() {

    private lateinit var localDataSource: LocalDataSource
    private lateinit var repository: Repository

    @Before
    public override fun setUp() {
        localDataSource = Mockito.mock(LocalDataSource::class.java)
        repository = Repository(localDataSource)
    }

    @Test
    fun `Should return empty list of categories`() {
        runBlocking {
            `when`(localDataSource.listAll()).thenReturn(flowOf(emptyList()))

            val response = repository.listAll().first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrDefault(emptyList()).isNullOrEmpty())
        }
    }

    @Test
    fun `Should return a list of categories`() {
        runBlocking {
            `when`(localDataSource.listAll()).thenReturn(flowOf(bookCategoriesList))
            val response = repository.listAll().first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().isNotEmpty())
            assertEquals(bookCategoriesList.size, response.getOrThrow().size)
        }
    }

    @Test
    fun `Should find a category using search`() {
        runBlocking {
            val resultsFlow = flowOf(bookCategoriesList.filter { it.title == "Android" })
            `when`(localDataSource.search(Mockito.anyString())).thenReturn(resultsFlow)
            val response = repository.search("*android*").first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().isNotEmpty())
            assertEquals(1, response.getOrThrow().size)
        }
    }

    @Test
    fun `Should return complete categories list after not finding search results`() {
        runBlocking {
            `when`(localDataSource.search(Mockito.anyString())).thenReturn(flowOf(emptyList()))
            `when`(localDataSource.listAll()).thenReturn(flowOf(bookCategoriesList))
            val response = repository.search("*lorem ipsum*").first()
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().isNotEmpty())
            assertEquals(bookCategoriesList.size, response.getOrThrow().size)
        }
    }

    @Test
    fun `Should return categories list after inserting data`() {
        runBlocking {
            `when`(localDataSource.listAll()).thenReturn(flowOf(bookCategoriesList))
            repository.populateList(RemoteData.bookCategoriesStream)
            val response = repository.listAll().first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().isNotEmpty())
            assertEquals(bookCategoriesList.size, response.getOrThrow().size)
        }
    }
}