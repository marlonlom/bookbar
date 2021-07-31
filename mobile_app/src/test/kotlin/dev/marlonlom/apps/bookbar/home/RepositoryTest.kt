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

import dev.marlonlom.apps.bookbar.utils.RemoteData.releasedBooksApiResponse
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

    private lateinit var remoteDataSource: ReleasedBooksContract.RemoteDataSource
    private lateinit var localDataSource: ReleasedBooksContract.LocalDataSource
    private lateinit var repository: ReleasedBooksContract.Repository

    @Before
    public override fun setUp() {
        remoteDataSource = Mockito.mock(ReleasedBooksContract.RemoteDataSource::class.java)
        localDataSource = Mockito.mock(ReleasedBooksContract.LocalDataSource::class.java)
        repository = ReleasedBooksContract.Repository(localDataSource, remoteDataSource)
    }

    @Test
    fun `Should return empty list of released books from remote data source`() {
        runBlocking {
            `when`(localDataSource.listAll()).thenReturn(flowOf(emptyList()))
            `when`(remoteDataSource.retrieveNewBooks())
                .thenReturn(flowOf(Result.failure(Exception("Released books not found ."))))

            val response = repository.retrieveNewBooks().first()
            assertNotNull(response)
            assertTrue(response.isFailure)
            assertEquals("Released books not found.", response.exceptionOrNull()!!.message)
        }
    }

    @Test
    fun `Should return a list of released books from local data source`() {
        runBlocking {
            `when`(localDataSource.listAll())
                .thenReturn(flowOf(releasedBooksApiResponse.books.map { toReleasedBook(it) }))

            val response = repository.retrieveNewBooks().first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(20, response.getOrThrow().size)
        }
    }

    @Test
    fun `Should return a list of released books from remote data source`() {
        runBlocking {
            `when`(localDataSource.listAll()).thenReturn(flowOf(emptyList()))
            `when`(remoteDataSource.retrieveNewBooks())
                .thenReturn(flowOf(Result.success(releasedBooksApiResponse.books)))

            val response = repository.retrieveNewBooks().first()
            assertNotNull(response)
            assertTrue(response.isSuccess)
            assertEquals(20, response.getOrThrow().size)
        }
    }

}