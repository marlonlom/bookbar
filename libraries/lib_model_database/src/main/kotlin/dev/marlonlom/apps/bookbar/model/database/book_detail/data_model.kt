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

package dev.marlonlom.apps.bookbar.model.database.book_detail

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO definition for book details database operations.
 *
 * @author marlonlom
 */
@Dao
interface BookDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BookDetail)

    @Query("DELETE FROM book_details WHERE isbn13 = :isbn")
    suspend fun delete(isbn: String)

    @Query("UPDATE book_details SET saved = :isSaved WHERE isbn13 = :isbn")
    suspend fun toggleSaved(isbn: String, isSaved: Boolean)

    @Query("SELECT * FROM book_details bdt WHERE bdt.isbn13 = :isbn")
    fun findByIsbn(isbn: String): Flow<List<BookDetail>>

    @Query("SELECT * FROM book_details bdt WHERE bdt.saved = 1")
    fun listSaved(): Flow<List<BookDetail>>

    @Query("SELECT * FROM book_details bdt WHERE book_details MATCH :query AND bdt.saved = 1")
    fun searchSaved(query: String): Flow<List<BookDetail>>

}

/**
 * Database entity class for book detailed information.
 *
 * @author marlonlom
 */
@Entity(tableName = "book_details")
@Fts4
data class BookDetail(
    @PrimaryKey
    @ColumnInfo(name = "rowid") val id: Int?,
    val isbn13: String,
    val isbn10: String,
    val title: String,
    val subtitle: String,
    val rating: String,
    val price: String,
    val language: String,
    val pages: String,
    val publisher: String,
    val year: String,
    val authors: String,
    val desc: String,
    val image: String,
    val url: String,
    val saved: Boolean? = false,
    val isFree: Boolean? = false,
    val freePdfUrl: String? = ""
) {
    companion object {

        val NONE: BookDetail
            get() = BookDetail(
                -1,"none", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", false
            )
    }
}