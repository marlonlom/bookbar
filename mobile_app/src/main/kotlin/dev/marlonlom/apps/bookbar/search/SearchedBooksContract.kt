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
import androidx.paging.*
import dev.marlonlom.apps.bookbar.model.network.BookListItem
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * MVVM Contract for searched books listing query.
 *
 * @author marlonlom
 */
@InternalCoroutinesApi
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

        /**
         * Retrieves searched books from repository.
         *
         * @param queryText search text
         */
        fun searchBooks(queryText: String): Flow<PagingData<BookListItem>> =
            repository.searchBooks(queryText).cachedIn(viewModelScope)
    }

    /**
     * Repository for searched books listing query.
     *
     * @author marlonlom
     */
    class Repository(private val bookStoreApi: BookStoreApi) {

        /**
         * Retrieves searched books from remote data source.
         *
         * @return flow with searched books query result
         */
        fun searchBooks(queryText: String): Flow<PagingData<BookListItem>> = Pager(
            pagingSourceFactory = { RemoteDataSource(bookStoreApi, queryText) },
            config = PagingConfig(pageSize = 10, enablePlaceholders = false)
        ).flow

    }

    /**
     * Remote data source for released books listing query.
     *
     * @author marlonlom
     */
    class RemoteDataSource(private val bookStoreApi: BookStoreApi, private val queryText: String) :
        PagingSource<Int, BookListItem>() {

        override fun getRefreshKey(state: PagingState<Int, BookListItem>): Int? =
            state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookListItem> = try {
            val page = params.key ?: 1
            val response = bookStoreApi.search(queryText, "$page")
            LoadResult.Page(
                data = response.books!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.books!!.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}