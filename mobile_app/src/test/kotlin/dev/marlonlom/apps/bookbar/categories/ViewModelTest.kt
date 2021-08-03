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

import dev.marlonlom.apps.bookbar.categories.BrowseCategoriesContract.Repository
import dev.marlonlom.apps.bookbar.categories.BrowseCategoriesContract.ViewModel
import dev.marlonlom.apps.bookbar.utils.RemoteData
import dev.marlonlom.apps.bookbar.utils.TestCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ViewModelTest : TestCase() {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: ViewModel
    private lateinit var repository: Repository

    @Before
    public override fun setUp() {
        super.setUp()
        repository = Mockito.mock(Repository::class.java)
        viewModel = ViewModel(repository)
    }

    @Test
    fun `Should return empty list of categories`() =
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.listAll()).thenReturn(flowOf(Result.success(emptyList())))
            viewModel.populateList(RemoteData.bookCategoriesStream)
            val response = viewModel.categories.first()
            assertNotNull(response)
            assertTrue(response.isNullOrEmpty())
        }

    @Test
    fun `Should return a list of categories after populating data`() =
        testCoroutineRule.runBlockingTest {
            val resultsFlow = flowOf(Result.success(RemoteData.bookCategoriesList))
            Mockito.`when`(repository.listAll()).thenReturn(resultsFlow)
            viewModel.populateList(RemoteData.bookCategoriesStream)
            val response = viewModel.categories.first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(209, response.size)
        }

    @Test
    fun `Should find a category using search`() = testCoroutineRule.runBlockingTest {
        val resultsFlow =
            flowOf(Result.success(RemoteData.bookCategoriesList.filter { it.title == "Android" }))
        Mockito.`when`(repository.search(Mockito.anyString())).thenReturn(resultsFlow)
        viewModel.searchCategories("*android*")
        viewModel.searchCategories("lorem ipsum")
        val response = viewModel.filteredCategories.first()
        assertNotNull(response)
        assertTrue(response.isNotEmpty())
        assertEquals(1, response.size)
    }


    @Test
    fun `Should return complete categories list after not finding search results`() =
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.search(Mockito.anyString()))
                .thenReturn(flowOf(Result.success(RemoteData.bookCategoriesList)))
            viewModel.searchCategories("lorem ipsum")
            val response = viewModel.filteredCategories.first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(RemoteData.bookCategoriesList.size, response.size)
        }

    @Test
    fun `Should return complete categories list after searching with empty text`() =
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.listAll())
                .thenReturn(flowOf(Result.success(RemoteData.bookCategoriesList)))
            viewModel.searchCategories("")
            val response = viewModel.filteredCategories.first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(RemoteData.bookCategoriesList.size, response.size)
        }

}