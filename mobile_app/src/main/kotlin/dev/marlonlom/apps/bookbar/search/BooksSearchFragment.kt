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

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dev.marlonlom.apps.bookbar.BuildConfig.API_BASE_URL
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.FragmentBooksSearchBinding
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.viewbindings.viewBinding
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber

/**
 * Saved books listing screen fragment class.
 *
 * @author marlonlom
 */
@InternalCoroutinesApi
@ExperimentalSerializationApi
class BooksSearchFragment : Fragment(R.layout.fragment_books_search) {

    private val uiBinding by viewBinding(FragmentBooksSearchBinding::bind)
    private val uiViewModel: SearchedBooksContract.ViewModel by navGraphViewModels(R.id.navigation_bookstore) { newViewModelFactory() }
    private val args: BooksSearchFragmentArgs by navArgs()

    private fun newViewModelFactory(): SearchedBooksContract.ViewModelFactory {
        Timber.w("Getting new ViewModel using network api ...")
        val apiService = BookStoreApi.Service.newService(API_BASE_URL.toHttpUrl())
        val repository = SearchedBooksContract.Repository(apiService)
        return SearchedBooksContract.ViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        setupScreen()
        obtainSearchedBooks(args.bookText)
    }

    private fun obtainSearchedBooks(bookText: String) {
        lifecycleScope.launch {
            uiViewModel.searchBooks(bookText).distinctUntilChanged().collectLatest { pagingData ->
                (uiBinding.listSearchedBooks.adapter as SearchedBooksListAdapter).submitData(
                    pagingData
                )
            }
        }
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackNavigation()
        }
    }

    private fun handleBackNavigation() {
        findNavController().popBackStack()
    }

    private fun setupScreen() {
        Timber.d("setupScreen")
        uiBinding.apply {
            btnScreenBack.apply {
                handleOnBackPressed()
            }
            listSearchedBooks.apply {
                setHasFixedSize(true)
                adapter = SearchedBooksListAdapter()
                layoutManager =
                    GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
            }
            editTextSearchSaved.addTextChangedListener {
                obtainSearchedBooks(it.toString())
            }
        }
    }

}