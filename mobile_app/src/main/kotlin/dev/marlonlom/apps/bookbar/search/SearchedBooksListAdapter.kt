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


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.textview.MaterialTextView
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.ItemSearchedBookBinding
import dev.marlonlom.apps.bookbar.model.network.BookListItem
import dev.marlonlom.apps.bookbar.search.SearchedBooksListAdapter.ViewHolder

/**
 * Recyclerview list adapter for searched books listing section in the respective screen.
 *
 * @author marlonlom
 */
class SearchedBooksListAdapter : PagingDataAdapter<BookListItem, ViewHolder>(DiffCallback()) {

    /**
     * Diff callback that compares two searched book list (the old one and the new one).
     *
     * @author marlonlom
     */
    class DiffCallback : DiffUtil.ItemCallback<BookListItem>() {

        override fun areItemsTheSame(oldItem: BookListItem, newItem: BookListItem): Boolean =
            oldItem.isbn13 == newItem.isbn13

        override fun areContentsTheSame(oldItem: BookListItem, newItem: BookListItem): Boolean =
            oldItem.contentEquals(newItem)
    }

    /**
     * ViewHolder that represents a searched book list item.
     *
     * @author marlonlom
     */
    class ViewHolder(itemUiBinding: ItemSearchedBookBinding) :
        RecyclerView.ViewHolder(itemUiBinding.root) {

        fun bind(item: BookListItem) {
            with(itemView) {
                findViewById<MaterialTextView>(R.id.text_searched_book_title).text =
                    item.title
                findViewById<MaterialTextView>(R.id.text_searched_book_price).text =
                    item.priceValue
                findViewById<ImageView>(R.id.text_searched_book_price).load(item.image) {
                    crossfade(true)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemSearchedBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}