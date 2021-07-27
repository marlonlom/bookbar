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

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.FragmentBooksSavedBinding
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.viewbindings.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import timber.log.Timber
import java.util.*

/**
 * Saved books screen fragment class.
 *
 * @author marlonlom
 */
@ExperimentalSerializationApi
class SavedBooksFragment : Fragment(R.layout.fragment_books_saved) {

    private val uiBinding by viewBinding(FragmentBooksSavedBinding::bind)
    private val uiViewModel: SavedBooksContract.ViewModel by navGraphViewModels(R.id.navigation_bookstore) { newViewModelFactory() }

    private fun newViewModelFactory(): SavedBooksContract.ViewModelFactory {
        Timber.w("Getting new ViewModel using local database for saved books listing ...")
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val localDataSource = SavedBooksContract.LocalDataSource(appDatabase)
        val repository = SavedBooksContract.Repository(localDataSource)
        return SavedBooksContract.ViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        setupScreen()
        obtainSavedBooks()
    }

    private fun obtainSavedBooks() {
        Timber.d("obtainSavedBooks")
        viewLifecycleOwner.lifecycleScope.launch {
            Timber.i("launch >> obtaining saved books")
            uiViewModel.books.collect { books ->
                processFoundBooks(books)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            Timber.i("launch >> obtaining book filtered saved books")
            uiViewModel.filteredBooks.collect { items ->
                Timber.i("launch >> non empty list of saved books? :${items.isNotEmpty()}")
                if (items.isNotEmpty()) {
                    processFoundBooks(items)
                }
            }
        }
        uiViewModel.retrieveSavedBooks()
    }

    private fun processFoundBooks(books: List<BookDetail>) {
        Timber.d("processFoundBooks -> books list is not empty? ${books.isNotEmpty()}")
        (uiBinding.listSavedBooks.adapter as SavedBooksListAdapter).submitList(books)
    }

    private fun setupScreen() {
        Timber.d("setupScreen")
        uiBinding.apply {
            listSavedBooks.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = SavedBooksListAdapter(handleSavedBookListItemClicked)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(), DividerItemDecoration.VERTICAL
                    )
                )
                val itemTouchHelper =
                    ItemTouchHelper(SwipeToDeleteCallback(handleUnsavedBookListItemClicked))
                itemTouchHelper.attachToRecyclerView(this)
            }
            editTextSearchSaved.apply {
                addTextChangedListener {
                    filterSavedBookByText(it.toString())
                }
            }
        }
    }

    private fun filterSavedBookByText(textToFilter: String) {
        Timber.d("filterSavedBookByText: $textToFilter")
        if (textToFilter.isNotEmpty()) {
            uiViewModel.searchSavedBooks(textToFilter)
        } else {
            uiViewModel.retrieveSavedBooks()
        }
    }

    private val handleSavedBookListItemClicked: (BookDetail) -> Unit = { book ->
        Timber.d("handleSavedBookListItemClicked: $book")
        findNavController().navigate(SavedBooksFragmentDirections.gotoSavedBookDetails(book.isbn13))
    }

    private val handleUnsavedBookListItemClicked: (Int, BookDetail) -> Unit =
        { deletedPosition, book ->
            Timber.d("handleUnsavedBookListItemClicked: $book")
            uiViewModel.toggleSaved(book.isbn13, false)
            Snackbar.make(
                requireActivity().window.decorView,
                R.string.text_saved_books_unsaved,
                Snackbar.LENGTH_LONG
            ).apply {
                setAction(R.string.label_saved_books_undo_delete) {
                    performSavedStateRestoration(deletedPosition, book)
                }
                show()
            }
        }

    private fun performSavedStateRestoration(deletedPosition: Int, book: BookDetail) {
        val savedBooksListAdapter = uiBinding.listSavedBooks.adapter as SavedBooksListAdapter
        val currentList = savedBooksListAdapter.currentList.toMutableList().apply {
            add(deletedPosition, book)
        }
        savedBooksListAdapter.submitList(Collections.unmodifiableList(currentList))
        uiViewModel.toggleSaved(book.isbn13, true)
    }
}