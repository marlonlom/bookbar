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

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.textview.MaterialTextView
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.ItemHomeReleasedBookBinding
import dev.marlonlom.apps.bookbar.home.ReleasedBooksListAdapter.ViewHolder
import dev.marlonlom.apps.bookbar.model.database.released_books.ReleasedBook
import timber.log.Timber

/**
 * Recyclerview list adapter for released books listing section in home screen.
 *
 * @author marlonlom
 */
class ReleasedBooksListAdapter(private val onClickItem: (ReleasedBook) -> Unit) :
    ListAdapter<ReleasedBook, ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        ItemHomeReleasedBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickItem)
    }

    /**
     * Diff callback that compares two released book list (the old one and the new one).
     *
     * @author marlonlom
     */
    class DiffCallback : DiffUtil.ItemCallback<ReleasedBook>() {

        override fun areItemsTheSame(oldItem: ReleasedBook, newItem: ReleasedBook): Boolean =
            oldItem.isbn13 == newItem.isbn13

        override fun areContentsTheSame(oldItem: ReleasedBook, newItem: ReleasedBook): Boolean =
            oldItem.contentEquals(newItem)
    }

    /**
     * ViewHolder that represents a released book list item.
     *
     * @author marlonlom
     */
    class ViewHolder(itemUiBinding: ItemHomeReleasedBookBinding) :
        RecyclerView.ViewHolder(itemUiBinding.root) {

        fun bind(item: ReleasedBook, onClickItem: (ReleasedBook) -> Unit) {
            itemView.apply {
                this.setOnClickListener {
                    Timber.d("setOnClickListener clicked")
                    onClickItem(item)
                }
                findViewById<MaterialTextView>(R.id.label_released_book_title).text =
                    item.title
                findViewById<ImageView>(R.id.image_released_book_cover).load(item.image) {
                    crossfade(true)
                    placeholder(R.drawable.img_book_placeholder)
                }

            }
        }
    }

}
