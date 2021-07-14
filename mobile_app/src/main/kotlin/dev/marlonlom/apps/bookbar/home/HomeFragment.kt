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

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dev.marlonlom.apps.bookbar.BuildConfig.API_BASE_URL
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.FragmentHomeBinding
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.released_books.ReleasedBook
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.viewbindings.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber

/**
 * Home fragment class.<br/>
 * Contains the following sections:
 * <ol>
 *     <li>Books search box by input text</li>
 *     <li>Books categories samples list</li>
 *     <li>Released books listing</li>
 * </ol>
 *
 * @author marlonlom
 */
@ExperimentalSerializationApi
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val uiBinding by viewBinding(FragmentHomeBinding::bind)
    private val uiViewModel: ReleasedBooksContract.ViewModel by navGraphViewModels(R.id.navigation_bookstore) { newViewModelFactory() }

    private fun newViewModelFactory(): ReleasedBooksContract.ViewModelFactory {
        Timber.w("Getting new ViewModel using network api and local database for released books listing ...")
        val apiService = BookStoreApi.Service.newService(API_BASE_URL.toHttpUrl())
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val localDataSource = ReleasedBooksContract.LocalDataSource(appDatabase)
        val remoteDataSource = ReleasedBooksContract.RemoteDataSource(apiService)
        val repository = ReleasedBooksContract.Repository(localDataSource, remoteDataSource)
        return ReleasedBooksContract.ViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        setupCategoriesSection()
        setupReleasedBooksListingSection()
        obtainReleasedBooks()
    }

    private fun obtainReleasedBooks() {
        Timber.d("obtainReleasedBooks")
        viewLifecycleOwner.lifecycleScope.launch {
            Timber.i("launch >> obtaining released books")
            uiViewModel.books.collect { books ->
                processFoundBooks(books)
            }
        }
        uiViewModel.retrieveNewBooks()
    }

    private fun processFoundBooks(books: List<ReleasedBook>) {
        Timber.d("processFoundBooks")
        if (books.isNotEmpty()) {
            (uiBinding.listReleasedBooks.adapter as ReleasedBooksListAdapter).submitList(books)
        } else {
            Timber.d("Empty list... display empty list info")
            Toast.makeText(
                requireContext(),
                "Empty list... display empty list info",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupCategoriesSection() {
        Timber.d("setupCategoriesSection")
        uiBinding.apply {
            btnGotoCategoriesBrowse.setOnClickListener {
                Timber.d("goto categories browsing destination.")
                findNavController().navigate(HomeFragmentDirections.gotoCategoriesBrowse())
            }
            chipSampleCategory01.setOnClickListener {
                val selectedTag =
                    requireContext().getString(R.string.tag_chip_sample_category_01)
                handleSampleCategoryItemClicked(selectedTag)
            }
            chipSampleCategory02.setOnClickListener {
                val selectedTag =
                    requireContext().getString(R.string.tag_chip_sample_category_02)
                handleSampleCategoryItemClicked(selectedTag)
            }
            chipSampleCategory03.setOnClickListener {
                val selectedTag =
                    requireContext().getString(R.string.tag_chip_sample_category_03)
                handleSampleCategoryItemClicked(selectedTag)
            }
            chipSampleCategory04.setOnClickListener {
                val selectedTag =
                    requireContext().getString(R.string.tag_chip_sample_category_04)
                handleSampleCategoryItemClicked(selectedTag)
            }
            chipSampleCategory05.setOnClickListener {
                val selectedTag =
                    requireContext().getString(R.string.tag_chip_sample_category_05)
                handleSampleCategoryItemClicked(selectedTag)
            }
        }
    }

    private val handleSampleCategoryItemClicked: (String) -> Unit = { category ->
        Timber.d("handleSampleCategoryItemClicked: $category")
        Toast.makeText(
            requireContext(),
            "handleSampleCategoryItemClicked: $category",
            Toast.LENGTH_LONG
        ).show()
    }


    private fun setupReleasedBooksListingSection() {
        Timber.d("setupReleasedBooksListingSection")
        uiBinding.apply {
            listReleasedBooks.apply {
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
                adapter = ReleasedBooksListAdapter(handleReleasedBookListItemClicked)
            }
        }
    }

    private val handleReleasedBookListItemClicked: (ReleasedBook) -> Unit = { bookListItem ->
        Timber.d("handleBookItemClicked: $bookListItem")
        findNavController().navigate(HomeFragmentDirections.gotoBookDetails(bookListItem.isbn13))
    }

}