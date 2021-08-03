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

package dev.marlonlom.apps.bookbar.search

import androidx.paging.PagingData
import dev.marlonlom.apps.bookbar.model.network.BookSearchApiResponse.Companion.EMPTY_RESPONSE
import dev.marlonlom.apps.bookbar.search.SearchedBooksContract.Repository
import dev.marlonlom.apps.bookbar.search.SearchedBooksContract.ViewModel
import dev.marlonlom.apps.bookbar.utils.RemoteData.searchedKotlinBooksApiResponse
import dev.marlonlom.apps.bookbar.utils.TestCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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

@InternalCoroutinesApi
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
        repository = mock(Repository::class.java)
        viewModel = ViewModel(repository)
    }

    @Test
    fun `Should not return search results using text at page 1`() =
        testCoroutineRule.runBlockingTest {
            `when`(repository.searchBooks(Mockito.anyString()))
                .thenReturn(flowOf(PagingData.from(EMPTY_RESPONSE.books!!)))
            viewModel.searchBooks("kotlin")
            val response = viewModel.books.first()
            assertNotNull(response)
        }

    @Test
    fun `Should return search result error using text at page 1`() =
        testCoroutineRule.runBlockingTest {
            `when`(repository.searchBooks(Mockito.anyString()))
                .thenReturn(flowOf(PagingData.empty()))
            viewModel.searchBooks("anything")
            val response = viewModel.books.first()
            assertNotNull(response)
        }

    @Test
    fun `Should return search results using text at page 1`() =
        testCoroutineRule.runBlockingTest {
            `when`(repository.searchBooks(Mockito.anyString())).thenReturn(
                flowOf(PagingData.from(searchedKotlinBooksApiResponse.books!!))
            )
            viewModel.searchBooks("kotlin")
            val response = viewModel.books.first()
            assertNotNull(response)
        }

}
