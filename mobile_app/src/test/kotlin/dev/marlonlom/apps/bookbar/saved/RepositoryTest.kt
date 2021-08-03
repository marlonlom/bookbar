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
import dev.marlonlom.apps.bookbar.saved.SavedBooksContract.LocalDataSource
import dev.marlonlom.apps.bookbar.saved.SavedBooksContract.Repository
import dev.marlonlom.apps.bookbar.utils.RemoteData
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest : TestCase() {

    private lateinit var localDataSource: LocalDataSource
    private lateinit var repository: Repository

    private val savedBooksList: List<BookDetail>
        get() = RemoteData.savedBooksApiResponse.books.map {
            toBookDetailEntity(it).copy(saved = true)
        }

    @Before
    public override fun setUp() {
        localDataSource = mock(LocalDataSource::class.java)
        repository = Repository(localDataSource)
    }

    @Test
    fun `Should return empty list of saved books`() {
        runBlockingTest {
            `when`(localDataSource.listSaved()).thenReturn(flowOf(emptyList()))
            val response = repository.retrieveSavedBooks().first()
            assertNotNull(response)
            assertTrue(response.isFailure)
            assertEquals("Saved books not found.", response.exceptionOrNull()!!.message)
        }
    }

    @Test
    fun `Should return a list of saved books`() {
        runBlockingTest {
            `when`(localDataSource.listSaved()).thenReturn(flowOf(savedBooksList))
            val response = repository.retrieveSavedBooks().first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().isNotEmpty())
            assertEquals(savedBooksList.size, response.getOrThrow().size)
        }
    }

    @Test
    fun `Should find a saved book using search`() {
        runBlockingTest {
            val resultsFlow = flowOf(savedBooksList.filter { it.title.startsWith("Kotlin") })
            `when`(localDataSource.searchSaved(Mockito.anyString())).thenReturn(resultsFlow)
            val response = repository.search("*kotlin*").first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().isNotEmpty())
            assertEquals(1, response.getOrThrow().size)
        }
    }

    @Test
    fun `Should return complete saved books list after not finding search results`() {
        runBlockingTest {
            `when`(localDataSource.searchSaved(Mockito.anyString())).thenReturn(flowOf(emptyList()))
            `when`(localDataSource.listSaved()).thenReturn(flowOf(savedBooksList))
            val response = repository.search("*lorem ipsum*").first()
            assertTrue(response.isSuccess)
            assertTrue(response.getOrThrow().isNotEmpty())
            assertEquals(savedBooksList.size, response.getOrThrow().size)
        }
    }

    @Test
    fun `Should save book state for selected isbn`() {
        runBlockingTest {
            repository.toggleSaved("1001622115721", true)
        }
    }

    @Test
    fun `Should unsave book state for selected isbn`() {
        runBlockingTest {
            repository.toggleSaved("1001622115721", false)
        }
    }
}