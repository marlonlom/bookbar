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

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.textview.MaterialTextView
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.ItemSavedBookBinding
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.saved.SavedBooksListAdapter.ViewHolder
import timber.log.Timber


/**
 * Recyclerview list adapter for saved books listing screen.
 *
 * @author marlonlom
 */
class SavedBooksListAdapter(private val itemClicked: (BookDetail) -> Unit) :
    ListAdapter<BookDetail, ViewHolder>(DiffCallback()) {

    /**
     * Diff callback that compares two books (the old one and the new one).
     *
     * @author marlonlom
     */
    class DiffCallback : DiffUtil.ItemCallback<BookDetail>() {

        override fun areItemsTheSame(oldItem: BookDetail, newItem: BookDetail): Boolean =
            oldItem.isbn13 == newItem.isbn13

        override fun areContentsTheSame(oldItem: BookDetail, newItem: BookDetail): Boolean =
            oldItem == newItem
    }


    /**
     * View holder that contains  single book row for saved books listing.
     *
     * @author marlonlom
     */
    class ViewHolder(uiBinding: ItemSavedBookBinding) : RecyclerView.ViewHolder(uiBinding.root) {
        fun bind(item: BookDetail, itemClicked: (BookDetail) -> Unit) {
            Timber.d("ViewHolder.bind")
            itemView.apply {
                setOnClickListener {
                    itemClicked(item)
                }
                findViewById<MaterialTextView>(R.id.text_saved_book_title).text = item.title
                findViewById<MaterialTextView>(R.id.text_saved_book_price).text = item.price
                findViewById<ImageView>(R.id.image_saved_book_cover).load(item.image) {
                    crossfade(true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemSavedBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemClicked)
    }


}

