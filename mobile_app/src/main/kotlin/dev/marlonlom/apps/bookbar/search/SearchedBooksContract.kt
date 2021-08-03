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

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.apps.bookbar.model.network.BookSearchApiResponse
import dev.marlonlom.apps.bookbar.model.network.BookSearchApiResponse.Companion.EMPTY_RESPONSE
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * MVVM Contract for searched books listing query.
 *
 * @author marlonlom
 */
interface SearchedBooksContract {

    /**
     * ViewModel factory for instantiating searched books query view model component.
     *
     * @author marlonlom
     */
    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T = when {
            (modelClass.isAssignableFrom(ViewModel::class.java)) -> ViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel not created")
        }
    }

    /**
     * ViewModel for searched books listing query.
     *
     * @author marlonlom
     */
    class ViewModel(private val repository: Repository) :
        androidx.lifecycle.ViewModel() {

        private var _books: MutableStateFlow<BookSearchApiResponse> =
            MutableStateFlow(EMPTY_RESPONSE)

        val books = _books.asStateFlow()

        /**
         * Retrieves searched books from repository.
         *
         * @param query search text
         * @param page contents page, or initial page (1)
         */
        fun searchBooks(query: String, page: Int? = 1) = viewModelScope.launch {
            repository.searchBooks(query, page).collect { result ->
                _books.value = result.getOrDefault(EMPTY_RESPONSE)
            }
        }
    }

    /**
     * Repository for searched books listing query.
     *
     * @author marlonlom
     */
    class Repository(private val remoteDataSource: RemoteDataSource) {

        /**
         * Retrieves searched books from remote data source.
         *
         * @return flow with searched books query result
         */
        fun searchBooks(query: String, page: Int? = 1)
                : Flow<Result<BookSearchApiResponse>> = flow {
            val value = remoteDataSource.searchBooks(query, page).first()
            emit(value)
        }
    }

    /**
     * Remote data source for released books listing query.
     *
     * @author marlonlom
     */
    class RemoteDataSource(private val bookStoreApi: BookStoreApi) {

        private val errorMessage = "Searched books not found."

        /**
         * Retrieves released books from api.
         *
         * @return flow with released books query result
         */
        suspend fun searchBooks(query: String, page: Int? = 1)
                : Flow<Result<BookSearchApiResponse>> = flow {
            val apiResult: Result<BookSearchApiResponse> = try {
                val newBooks = bookStoreApi.search(query, "$page")
                val isSuccess = newBooks.error == "0" && newBooks.books!!.isNotEmpty()
                if (isSuccess) Result.success(newBooks)
                else Result.success(EMPTY_RESPONSE)
            } catch (exception: Exception) {
                Result.failure(Exception(errorMessage, exception))
            }
            emit(apiResult)
        }
    }
}