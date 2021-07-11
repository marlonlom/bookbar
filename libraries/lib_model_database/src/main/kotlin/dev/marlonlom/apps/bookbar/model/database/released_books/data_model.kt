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

package dev.marlonlom.apps.bookbar.model.database.released_books

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReleasedBooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ReleasedBook)

    @Query("DELETE FROM released_books")
    suspend fun deleteAll()

    @Query("SELECT * FROM released_books")
    fun listAll(): Flow<List<ReleasedBook>>

    @Query("SELECT * FROM released_books rb WHERE rb.isbn13 = :isbn")
    fun findByIsbn(isbn: String): Flow<List<ReleasedBook>>

}

@Entity(tableName = "released_books")
data class ReleasedBook(
    @PrimaryKey
    val isbn13: String,
    val title: String,
    val subtitle: String,
    val price: String,
    val image: String,
    val url: String
) {
    /**
     * Return true/false if contents of two instances of book list item class are the same.
     *
     * @param anotherItem another instance of book list item class
     *
     * @return true/false
     */
    fun contentEquals(anotherItem: ReleasedBook): Boolean =
        (this.isbn13 == anotherItem.isbn13 && this.title == anotherItem.title
                && this.subtitle == anotherItem.subtitle && this.image == anotherItem.image
                && this.url == anotherItem.url && this.price == anotherItem.price)
}
