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

package dev.marlonlom.apps.bookbar.detail

import dev.marlonlom.apps.bookbar.detail.BookDetailsContract.Repository
import dev.marlonlom.apps.bookbar.detail.BookDetailsContract.ViewModel
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.utils.RemoteData
import dev.marlonlom.apps.bookbar.utils.TestCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
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
        repository = mock(Repository::class.java)
        viewModel = ViewModel(repository)
    }

    @Test
    fun `Should perform successfully ui state clearing`() =
        testCoroutineRule.runBlockingTest {
            viewModel.clearState()
            val aBook = viewModel.book.first()
            val aBookSavedState = viewModel.bookSaved.first()
            assertNotNull(aBook)
            assertEquals(BookDetail.NONE, aBook)
            assertFalse(aBookSavedState)
        }

    @Test
    fun `Should not return book detail using selected isbn`() =
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.findBook(anyString()))
                .thenReturn(flowOf(Result.failure(Exception("Book information not found."))))
            viewModel.retrieveBook("1234567890123")
            val response = viewModel.book.first()
            assertNotNull(response)
            assertEquals(BookDetail.NONE, response)
        }

    @Test
    fun `Should return book detail using selected isbn`() =
        testCoroutineRule.runBlockingTest {
            val bookDetail = toBookDetailEntity(RemoteData.purchasableBookApiResponse)
            Mockito.`when`(repository.findBook(anyString()))
                .thenReturn(flowOf(Result.success(bookDetail)))
            viewModel.retrieveBook(bookDetail.isbn13)
            val response = viewModel.book.first()
            assertNotNull(response)
            assertNotEquals(BookDetail.NONE, response)
        }


    @Test
    fun `Should save book detail state for selected isbn`() = testCoroutineRule.runBlockingTest {
        viewModel.toggleSaved("1001622115721")
        val aBookSavedState = viewModel.bookSaved.first()
        assertTrue(aBookSavedState)
    }


    @Test
    fun `Should unsave book detail state for selected isbn`() = testCoroutineRule.runBlockingTest {
        viewModel.toggleSaved("1001622115721")
        viewModel.toggleSaved("1001622115721")
        val aBookSavedState = viewModel.bookSaved.first()
        assertFalse(aBookSavedState)
    }

}