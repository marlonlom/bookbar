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

package dev.marlonlom.apps.bookbar.model.database.categories

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BookCategory)

    @Query("DELETE FROM book_categories")
    suspend fun deleteAll()

    @Query("SELECT * FROM book_categories")
    fun listAll(): Flow<List<BookCategory>>

    @Query("SELECT * FROM book_categories bc WHERE bc.tag = :categoryTag")
    fun findByTag(categoryTag: String): Flow<List<BookCategory>>

    @Query("SELECT * FROM book_categories bc WHERE bc.tag LIKE '%:someText%'")
    fun findByText(someText: String): Flow<List<BookCategory>>

}

@Entity(tableName = "book_categories")
data class BookCategory(
    @PrimaryKey
    val tag: String,
    val title: String
)
