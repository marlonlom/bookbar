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

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * MVVM Contract for saved books listing query.
 *
 * @author marlonlom
 */
interface SavedBooksContract {

    /**
     * ViewModel factory for instantiating saved books query view model component.
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
     * ViewModel for saved books listing query.
     *
     * @author marlonlom
     */
    class ViewModel(private val repository: Repository) :
        androidx.lifecycle.ViewModel() {

        private var _books: MutableStateFlow<List<BookDetail>> =
            MutableStateFlow(emptyList())
        val books: StateFlow<List<BookDetail>> = _books.asStateFlow()
        private var _filteredBooks: MutableStateFlow<List<BookDetail>> =
            MutableStateFlow(emptyList())
        val filteredBooks: StateFlow<List<BookDetail>> = _filteredBooks.asStateFlow()

        /**
         * Retrieves saved books from remote data source.
         */
        fun retrieveSavedBooks() = viewModelScope.launch {
            repository.retrieveSavedBooks().collect {
                _books.value = it.getOrDefault(emptyList())
            }
        }

        /**
         * Toggles the saved state of a book saved in the database.
         *
         * @param isbn books isbn code
         * @param isSaved true/false
         */
        fun toggleSaved(isbn: String, isSaved: Boolean) = viewModelScope.launch {
            repository.toggleSaved(isbn, isSaved)
        }

        fun searchSavedBooks(query: String) = viewModelScope.launch {
            Timber.d("searchSavedBooks($query)")
            val defaultValues = _books.value
            if (query.isNotEmpty()) {
                repository.search("*$query*").collect { list ->
                    _filteredBooks.value = list.getOrDefault(defaultValues)
                }
            } else {
                retrieveSavedBooks()
            }
        }
    }

    /**
     * Repository for saved books listing query.
     *
     * @author marlonlom
     */
    class Repository(private val localDataSource: LocalDataSource) {

        private val errorMessage = "Saved books not found."

        /**
         * Retrieves saved books from remote data source.
         *
         * @return flow with saved books query result
         */
        fun retrieveSavedBooks(): Flow<Result<List<BookDetail>>> = flow {
            val apiResult: Result<List<BookDetail>> = try {
                val localResults: List<BookDetail> = localDataSource.listSaved().first()
                when {
                    localResults.isNullOrEmpty() -> Result.failure(Exception(errorMessage))
                    else -> Result.success(localResults)
                }
            } catch (exception: Exception) {
                Result.failure(Exception(errorMessage, exception))
            }
            emit(apiResult)
        }

        suspend fun toggleSaved(isbn: String, isSaved: Boolean) =
            localDataSource.toggleSaved(isbn, isSaved)

        fun search(query: String): Flow<Result<List<BookDetail>>> = flow {
            val list = localDataSource.searchSaved(query).first()
            val searchResults = when {
                list.isEmpty() -> {
                    val defaultList = localDataSource.listSaved().first()
                    Result.success(defaultList)
                }
                else -> Result.success(list)
            }
            emit(searchResults)
        }

    }

    /**
     * Local data source for saved books listing query.
     *
     * @author marlonlom
     */
    class LocalDataSource(private val appDatabase: AppDatabase) {
        fun listSaved(): Flow<List<BookDetail>> = appDatabase.bookDetailsDao().listSaved()
        fun searchSaved(query: String) = appDatabase.bookDetailsDao().searchSaved(query)
        suspend fun toggleSaved(isbn: String, isSaved: Boolean) =
            appDatabase.bookDetailsDao().toggleSaved(isbn, isSaved)
    }
}