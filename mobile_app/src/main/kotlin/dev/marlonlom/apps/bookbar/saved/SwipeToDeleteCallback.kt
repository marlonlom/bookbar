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

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import java.util.*


/**
 * Swipe to delete Callback for the given drag and swipe allowance.
 *
 * @author marlonlom
 */
class SwipeToDeleteCallback(private val itemUnsaved: (Int, BookDetail) -> Unit) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private lateinit var icon: Drawable
    private lateinit var background: ColorDrawable

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val bindingAdapterPosition = viewHolder.bindingAdapterPosition
        val savedBooksListAdapter = viewHolder.bindingAdapter as SavedBooksListAdapter
        val bookDetail = savedBooksListAdapter.currentList[bindingAdapterPosition]
        val currentList = savedBooksListAdapter.currentList.toMutableList().apply {
            removeAt(bindingAdapterPosition)
        }
        savedBooksListAdapter.submitList(Collections.unmodifiableList(currentList))
        itemUnsaved(bindingAdapterPosition,bookDetail)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView: View = viewHolder.itemView

        background = getBackground(recyclerView.context)
        icon = getDeleteIcon(recyclerView.context)

        val top = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val left = itemView.width - icon.intrinsicWidth - (itemView.height - icon.intrinsicHeight) / 2
        val right = left + icon.intrinsicHeight
        val bottom = top + icon.intrinsicHeight

        if (dX < 0) {
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            icon.setBounds(left, top, right, bottom)
        } else if (dX > 0) {
            background.setBounds(itemView.left + dX.toInt(), itemView.top, itemView.left, itemView.bottom)
            icon.setBounds(top, top, top, bottom)
        }

        background.draw(c)
        icon.draw(c)

    }

    private fun getBackground(context: Context): ColorDrawable {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.colorError, typedValue, true)
        return ColorDrawable(typedValue.data)
    }

    private fun getDeleteIcon(context: Context): Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_saved_book_delete)!!

}
