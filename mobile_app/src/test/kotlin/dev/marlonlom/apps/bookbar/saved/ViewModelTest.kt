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

package dev.marlonlom.apps.bookbar.saved

import dev.marlonlom.apps.bookbar.detail.toBookDetailEntity
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.saved.SavedBooksContract.Repository
import dev.marlonlom.apps.bookbar.saved.SavedBooksContract.ViewModel
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ViewModelTest : TestCase() {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: ViewModel
    private lateinit var repository: Repository

    private val savedBooksList: List<BookDetail>
        get() = RemoteData.savedBooksApiResponse.books.map {
            toBookDetailEntity(it).copy(saved = true)
        }

    @Before
    public override fun setUp() {
        super.setUp()
        repository = mock(Repository::class.java)
        viewModel = ViewModel(repository)
    }

    @Test
    fun `Should return empty list of saved books`() =
        testCoroutineRule.runBlockingTest {
            `when`(repository.retrieveSavedBooks())
                .thenReturn(flowOf(Result.success(emptyList())))
            viewModel.retrieveSavedBooks()
            val response = viewModel.books.first()
            assertNotNull(response)
            assertTrue(response.isNullOrEmpty())
        }

    @Test
    fun `Should return a list of saved books`() =
        testCoroutineRule.runBlockingTest {
            val resultsFlow = flowOf(Result.success(savedBooksList))
            `when`(repository.retrieveSavedBooks()).thenReturn(resultsFlow)
            viewModel.retrieveSavedBooks()
            val response = viewModel.books.first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(2, response.size)
        }

    @Test
    fun `Should find a saved book using search`() = testCoroutineRule.runBlockingTest {
        val resultsFlow =
            flowOf(Result.success(savedBooksList.filter { it.title.startsWith("Kotlin") }))
        `when`(repository.search(Mockito.anyString())).thenReturn(resultsFlow)
        viewModel.searchSavedBooks("kotlin")
        val response = viewModel.filteredBooks.first()
        assertNotNull(response)
        assertTrue(response.isNotEmpty())
        assertEquals(1, response.size)
    }

    @Test
    fun `Should return complete saved books list after not finding search results`() =
        testCoroutineRule.runBlockingTest {
            `when`(repository.search(Mockito.anyString()))
                .thenReturn(flowOf(Result.success(savedBooksList)))
            viewModel.searchSavedBooks("lorem ipsum")
            val response = viewModel.filteredBooks.first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(2, response.size)
        }

    @Test
    fun `Should return complete saved books list after searching using empty text`() =
        testCoroutineRule.runBlockingTest {
            `when`(repository.retrieveSavedBooks())
                .thenReturn(flowOf(Result.success(savedBooksList)))
            viewModel.searchSavedBooks("")
            val response = viewModel.filteredBooks.first()
            assertNotNull(response)
            assertTrue(response.isNotEmpty())
            assertEquals(2, response.size)
        }

    @Test
    fun `Should save book state for selected isbn`() = testCoroutineRule.runBlockingTest {
        viewModel.toggleSaved("1001622115721", true)
    }

    @Test
    fun `Should unsave book state for selected isbn`() = testCoroutineRule.runBlockingTest {
        viewModel.toggleSaved("1001622115721", false)
    }

}