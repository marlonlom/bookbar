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

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.model.network.BookDetailApiResponse
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * MVVM Contract for book details query.
 *
 * @author marlonlom
 */
interface BookDetailsContract {

    /**
     * ViewModel factory for instantiating book details query view model component.
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
     * ViewModel for book details query.
     *
     * @author marlonlom
     */
    class ViewModel(private val repository: Repository) : androidx.lifecycle.ViewModel() {

        private var _book: MutableStateFlow<BookDetail> = MutableStateFlow(BookDetail.NONE)
        val book: StateFlow<BookDetail> get() = _book.asStateFlow()
        private var _bookSaved: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val bookSaved: StateFlow<Boolean> get() = _bookSaved.asStateFlow()

        /**
         * Clears the ui state.
         */
        fun clearState() = viewModelScope.launch {
            _book.value = BookDetail.NONE
            _bookSaved.value = false
        }

        /**
         * Retrieves released books from remote data source.
         */
        fun retrieveBook(isbn: String) = viewModelScope.launch {
            repository.findBook(isbn).collect {
                val bookDetail = it.getOrDefault(BookDetail.NONE)
                _book.value = bookDetail
                _bookSaved.value = bookDetail.saved!!
            }
        }

        /**
         * Toggles the saved stated for selected book.
         *
         * @param isbn book ISBN
         */
        fun toggleSaved(isbn: String) = viewModelScope.launch {
            val isSaved = !_bookSaved.value
            repository.toggleSaved(isbn, isSaved)
            _bookSaved.value = isSaved
        }

    }

    /**
     * Repository for book details query.
     *
     * @author marlonlom
     */
    class Repository(
        private val localDataSource: LocalDataSource,
        private val remoteDataSource: RemoteDataSource
    ) {

        private val errorMessage = "Book information not found."

        /**
         * Toggles the saved stated for selected book.
         *
         * @param isbn book ISBN
         * @param isSaved true/false if book is saved
         */
        suspend fun toggleSaved(isbn: String, isSaved: Boolean) =
            localDataSource.toggleSaved(isbn, isSaved)

        /**
         * Retrieves released books from remote data source.
         *
         * @param isbn book ISBN
         *
         * @return flow with book details query result
         */
        suspend fun findBook(isbn: String): Flow<Result<BookDetail>> = flow {
            val apiResult: Result<BookDetail> =
                try {
                    val localResults = localDataSource.findSingle(isbn).first()
                    when {
                        localResults.isNullOrEmpty() -> findBookRemote(isbn)
                        else -> Result.success(localResults[0])
                    }
                } catch (exception: Exception) {
                    Result.failure(Exception(errorMessage, exception))
                }
            emit(apiResult)
        }

        private suspend fun findBookRemote(isbn: String): Result<BookDetail> {
            Timber.d("findBookRemote('$isbn')")
            val remoteResult = remoteDataSource.findBook(isbn).first()
            saveBookDetailIfSuccess(remoteResult)
            return when {
                remoteResult.isSuccess -> Result.success(toBookDetailEntity(remoteResult.getOrThrow()))
                else -> Result.failure(Exception(errorMessage))
            }
        }

        private suspend fun saveBookDetailIfSuccess(remoteResult: Result<BookDetailApiResponse>) {
            val success = remoteResult.isSuccess
            Timber.d("saveBookDetailIfSuccess (success=$success)")
            if (success) {
                localDataSource.insert(toBookDetailEntity(remoteResult.getOrThrow()))
            }
        }

        private fun toBookDetailEntity(item: BookDetailApiResponse) = BookDetail(
            isbn13 = item.isbn13!!, isbn10 = item.isbn10!!, title = item.title!!,
            subtitle = item.subtitle!!, rating = item.rating!!, price = item.priceValue!!,
            language = item.language!!, pages = item.pages!!, publisher = item.publisher!!,
            year = item.year!!, authors = item.authors!!, desc = item.desc!!,
            image = item.image!!, url = item.url!!, saved = false,
            isFree = item.pdf?.hasFreeEBook, freePdfUrl = item.pdf?.freeEBook
        )

    }

    /**
     * Remote data source for book details query.
     *
     * @author marlonlom
     */
    class RemoteDataSource(private val bookStoreApi: BookStoreApi) {

        private val errorMessage = "Book information not found."

        /**
         * Find book details from api using the requested ISBN.
         *
         * @return flow with book details query result
         */
        suspend fun findBook(isbn: String): Flow<Result<BookDetailApiResponse>> = flow {
            val apiResult: Result<BookDetailApiResponse> = try {
                val foundBook = bookStoreApi.getBookDetail(isbn)
                if (foundBook.error == "0") Result.success(foundBook)
                else Result.failure(Exception(errorMessage))
            } catch (exception: Exception) {
                Timber.e(exception)
                Result.failure(Exception(errorMessage, exception))
            }
            emit(apiResult)
        }
    }

    /**
     * Local data source for book details query.
     *
     * @author marlonlom
     */
    class LocalDataSource(private val appDatabase: AppDatabase) {

        fun findSingle(isbn: String) = appDatabase.bookDetailsDao().findByIsbn(isbn)

        suspend fun toggleSaved(isbn: String, isSaved: Boolean) =
            appDatabase.bookDetailsDao().toggleSaved(isbn, isSaved)

        suspend fun insert(item: BookDetail) = appDatabase.bookDetailsDao().insert(item)
    }
}
