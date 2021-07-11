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

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.released_books.ReleasedBook
import dev.marlonlom.apps.bookbar.model.network.BookListItem
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

private const val errorMessage = "Released books not found."

/**
 * MVVM Contract for released books listing query.
 *
 * @author marlonlom
 */
interface ReleasedBooksContract {

    /**
     * ViewModel factory for instantiating released books query view model component.
     *
     * @author marlonlom
     */
    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T = when {
            (modelClass.isAssignableFrom(ViewModel::class.java)) -> ViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel not created")
        }
    }

    /**
     * ViewModel for released books listing query.
     *
     * @author marlonlom
     */
    class ViewModel(private val repository: Repository) : androidx.lifecycle.ViewModel() {

        private var _books: MutableStateFlow<List<ReleasedBook>> =
            MutableStateFlow(emptyList())

        val books: StateFlow<List<ReleasedBook>> = _books.asStateFlow()

        /**
         * Retrieves released books from remote data source.
         */
        fun retrieveNewBooks() = viewModelScope.launch {
            repository.retrieveNewBooks().collect {
                _books.value = it.getOrDefault(emptyList())
            }
        }
    }

    /**
     * Repository for released books listing query.
     *
     * @author marlonlom
     */
    class Repository(
        private val localDataSource: LocalDataSource,
        private val remoteDataSource: RemoteDataSource
    ) {

        /**
         * Retrieves released books from remote data source.
         *
         * @return flow with released books query result
         */
        @Suppress("LiftReturnOrAssignment")
        suspend fun retrieveNewBooks(): Flow<Result<List<ReleasedBook>>> = flow {
            val apiResult: Result<List<ReleasedBook>>? =
                try {
                    val localResults: List<ReleasedBook> = localDataSource.listAll().first()
                    when {
                        localResults.isNullOrEmpty() -> retrieveNewBooksRemote()
                        else -> Result.success(localResults)
                    }
                } catch (exception: Exception) {
                    Result.failure(Exception(errorMessage, exception))
                }

            emit(apiResult ?: Result.failure(Exception(errorMessage)))
        }

        private suspend fun retrieveNewBooksRemote(): Result<List<ReleasedBook>> {
            val remoteResult = remoteDataSource.retrieveNewBooks().first()
            return when {
                remoteResult.isSuccess -> Result.success(
                    remoteResult.getOrThrow().map { toReleasedBook(it) })
                else -> Result.failure(Exception(errorMessage))
            }
        }

        private fun toReleasedBook(item: BookListItem) =
            ReleasedBook(
                item.isbn13, item.title, item.subtitle, item.priceValue, item.image, item.url
            )

        @Suppress("unused")
        suspend fun submitList(anotherList: List<BookListItem>) {
            Timber.d("submitList")
            localDataSource.deleteAll()
            anotherList.forEach {
                localDataSource.insert(toReleasedBook(it))
            }
        }
    }

    /**
     * Remote data source for released books listing query.
     *
     * @author marlonlom
     */
    class RemoteDataSource(private val bookStoreApi: BookStoreApi) {

        /**
         * Retrieves released books from api.
         *
         * @return flow with released books query result
         */
        @Suppress("LiftReturnOrAssignment")
        suspend fun retrieveNewBooks(): Flow<Result<List<BookListItem>>> = flow {
            val apiResult: Result<List<BookListItem>>? =
                try {
                    val newBooks = bookStoreApi.getNewBooks()
                    val isSuccess = newBooks.error == "0" && newBooks.books.isNotEmpty()
                    if (isSuccess) Result.success(newBooks.books)
                    else Result.failure(Exception(errorMessage))
                } catch (exception: Exception) {
                    Result.failure(Exception(errorMessage, exception))
                }
            emit(apiResult ?: Result.failure(Exception(errorMessage)))
        }
    }


    /**
     * Local data source for released books listing query.
     *
     * @author marlonlom
     */
    class LocalDataSource(private val appDatabase: AppDatabase) {
        fun listAll(): Flow<List<ReleasedBook>> = appDatabase.releasedBooksDao().listAll()
        suspend fun deleteAll() = appDatabase.releasedBooksDao().deleteAll()
        suspend fun insert(item: ReleasedBook) = appDatabase.releasedBooksDao().insert(item)
    }
}
