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

import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.search.SearchedBooksContract.Repository
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@Suppress("EXPERIMENTAL_dataSource_USAGE")
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest : TestCase() {

    private lateinit var repository: Repository
    private lateinit var api: BookStoreApi

    @Before
    public override fun setUp() {
        api = mock(BookStoreApi::class.java)
        repository = Repository(api)
    }

    @Test
    fun `Should not return search results using text at page 1`() {
        runBlockingTest {
            val response = repository.searchBooks("kotlin").first()
            assertNotNull(response)
        }
    }

    @Test
    fun `Should return search result error using text at page 1`() {
        runBlockingTest {
            val response = repository.searchBooks("anything").first()
            assertNotNull(response)
        }
    }

    @Test
    fun `Should return search results using text at page 1`() {
        runBlockingTest {
            val response = repository.searchBooks("kotlin").first()
            assertNotNull(response)
        }
    }

}
