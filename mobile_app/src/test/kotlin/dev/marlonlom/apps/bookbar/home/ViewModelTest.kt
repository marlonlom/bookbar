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

package dev.marlonlom.apps.bookbar.home

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

    private lateinit var viewModel: ReleasedBooksContract.ViewModel
    private lateinit var repository: ReleasedBooksContract.Repository

    @Before
    public override fun setUp() {
        super.setUp()
        repository = Mockito.mock(ReleasedBooksContract.Repository::class.java)
        viewModel = ReleasedBooksContract.ViewModel(repository)
    }

    @Test
    fun `Should return empty list of released books`() =
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.retrieveNewBooks())
                .thenReturn(flowOf(Result.success(emptyList())))

            viewModel.retrieveNewBooks()
            val response = viewModel.books.first()

            assertNotNull(response)
            assertTrue(response.isNullOrEmpty())
        }

    @Test
    fun `Should return a list of released books from local data source`() =
        testCoroutineRule.runBlockingTest {
            val resultsFlow = flowOf(
                Result.success(RemoteData.releasedBooksApiResponse.books.map {
                    toReleasedBook(it)
                })
            )
            Mockito.`when`(repository.retrieveNewBooks())
                .thenReturn(resultsFlow)

            viewModel.retrieveNewBooks()
            val response = viewModel.books.first()

            assertNotNull(response)
            assertFalse(response.isNullOrEmpty())
            assertEquals(20, response.size)
        }

}