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

package dev.marlonlom.apps.bookbar.categories

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.categories.BookCategory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.InputStream

/**
 * MVVM Contract for browse categories listing.
 *
 * @author marlonlom
 */
interface BrowseCategoriesContract {
    /**
     * ViewModel factory for instantiating browse categories listing view model component.
     *
     * @author marlonlom
     */
    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T = when {
            (modelClass.isAssignableFrom(ViewModel::class.java)) -> ViewModel(
                repository
            ) as T
            else -> throw IllegalArgumentException("ViewModel not created")
        }
    }

    /**
     * ViewModel for browse categories listing.
     *
     * @author marlonlom
     */
    class ViewModel(private val repository: Repository) :
        androidx.lifecycle.ViewModel() {

        private var _categories: MutableStateFlow<List<BookCategory>> =
            MutableStateFlow(emptyList())
        val categories: StateFlow<List<BookCategory>> = _categories.asStateFlow()

        private var _filteredCategories: MutableStateFlow<List<BookCategory>> =
            MutableStateFlow(emptyList())
        val filteredCategories: StateFlow<List<BookCategory>> = _filteredCategories.asStateFlow()

        /**
         * Populates categories list from input stream.
         * @param inputStream json content of book categories as a Stream.
         */
        fun populateList(inputStream: InputStream) = viewModelScope.launch {
            Timber.d("populateList from ViewModel")
            repository.populateList(inputStream)
            fetchCategories()
        }

        private suspend fun fetchCategories() {
            repository.listAll().collect {
                _categories.value = it.getOrDefault(emptyList())
            }
        }

        fun searchCategories(query: String) = viewModelScope.launch {
            val defaultValues = _categories.value
            if (query.isNotEmpty()) {
                repository.search("*$query*").collect { list ->
                    _filteredCategories.value = list.getOrDefault(defaultValues)
                }
            } else {
                fetchCategories()
                _filteredCategories.value = _categories.value
            }
        }

        fun clearFilteredResults() {
            _filteredCategories.value = emptyList()
            _categories.value = emptyList()
        }

    }

    /**
     * Repository for browse categories listing.
     *
     * @author marlonlom
     */
    class Repository(private val localDataSource: LocalDataSource) {

        fun listAll(): Flow<Result<List<BookCategory>>> = flow {
            val list = localDataSource.listAll().first()
            emit(Result.success(list))
        }

        fun search(query: String): Flow<Result<List<BookCategory>>> = flow {
            val list = localDataSource.search(query).first()
            if (list.isEmpty()) {
                val defaultList = localDataSource.listAll().first()
                emit(Result.success(defaultList))
            } else {
                emit(Result.success(list))
            }
        }

        suspend fun populateList(inputStream: InputStream) {
            Timber.d("populateList from Repository")
            localDataSource.deleteAll()
            val list = convertToList(inputStream)
            localDataSource.insertCategories(list)
        }

        private fun convertToList(jsonContents: InputStream): List<BookCategory> =
            jsonContents.let {
                return mutableListOf<BookCategory>().apply {
                    it.bufferedReader().useLines { lines ->
                        lines.forEachIndexed { index, line ->
                            val categoryPart = line.split(";")
                            this.add(
                                BookCategory(
                                    id = index + 1,
                                    tag = categoryPart[1],
                                    title = categoryPart[0]
                                )
                            )
                        }
                    }
                }
            }
    }

    /**
     * Local data source for browse categories listing.
     *
     * @author marlonlom
     */
    class LocalDataSource(private val appDatabase: AppDatabase) {
        fun listAll() = appDatabase.categoriesDao().listAll()
        fun search(query: String) = appDatabase.categoriesDao().search(query)
        suspend fun deleteAll() = appDatabase.categoriesDao().deleteAll()
        suspend fun insertCategories(items: List<BookCategory>) =
            appDatabase.categoriesDao().insertCategories(items)
    }

}