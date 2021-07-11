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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.ItemCategoriesBrowseRowBinding
import dev.marlonlom.apps.bookbar.model.database.categories.BookCategory
import timber.log.Timber

/**
 * Recyclerview list adapter for book categories listing section in category browse screen.
 *
 * @author marlonlom
 */
class BookCategoriesListAdapter(private val onClickItem: (BookCategory) -> Unit) :
    ListAdapter<BookCategory, BookCategoriesListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        ItemCategoriesBrowseRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickItem)
    }

    /**
     * Diff callback that compares two book categories (the old one and the new one).
     *
     * @author marlonlom
     */
    class DiffCallback : DiffUtil.ItemCallback<BookCategory>() {

        override fun areItemsTheSame(oldItem: BookCategory, newItem: BookCategory): Boolean =
            oldItem.tag == newItem.tag

        override fun areContentsTheSame(oldItem: BookCategory, newItem: BookCategory): Boolean =
            oldItem.tag == newItem.tag && oldItem.title == newItem.title
    }

    /**
     * ViewHolder that represents a book category list item.
     *
     * @author marlonlom
     */
    class ViewHolder(itemUiBinding: ItemCategoriesBrowseRowBinding) :
        RecyclerView.ViewHolder(itemUiBinding.root) {

        fun bind(item: BookCategory, onClickItem: (BookCategory) -> Unit) {
            itemView.apply {
                this.setOnClickListener {
                    Timber.d("setOnClickListener clicked")
                    onClickItem(item)
                }
                findViewById<MaterialTextView>(R.id.text_book_category).text = item.title
            }
        }
    }

}
