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

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.FragmentCategoriesBrowseBinding
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.categories.BookCategory
import dev.marlonlom.apps.bookbar.viewbindings.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Fragment class for browse categories screen.
 * @author marlonlom
 *
 */
class BrowseCategoriesFragment : Fragment(R.layout.fragment_categories_browse) {
    private val uiBinding by viewBinding(FragmentCategoriesBrowseBinding::bind)
    private val uiViewModel: BrowseCategoriesContract.ViewModel by navGraphViewModels(R.id.navigation_bookstore) { newViewModelFactory() }

    private fun newViewModelFactory(): BrowseCategoriesContract.ViewModelFactory {
        Timber.w("Getting new ViewModel using network api and local database for categories listing ...")
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val localDataSource = BrowseCategoriesContract.LocalDataSource(appDatabase)
        val repository = BrowseCategoriesContract.Repository(localDataSource)
        return BrowseCategoriesContract.ViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        handleOnBackPressed()
        setupScreen()
        obtainAvailableCategories()
    }

    private fun obtainAvailableCategories() {
        Timber.d("obtainAvailableCategories")
        viewLifecycleOwner.lifecycleScope.launch {
            Timber.i("launch >> obtaining book categories")
            uiViewModel.categories.collect { items ->
                processFoundCategories(items)
            }
        }
        uiViewModel.populateList(resources.openRawResource(R.raw.book_categories))
    }

    private fun processFoundCategories(items: List<BookCategory>) {
        Timber.d("processFoundCategories")
        (uiBinding.listCategories.adapter as BookCategoriesListAdapter).submitList(items)
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun setupScreen() {
        Timber.d("setupScreen")

        uiBinding.apply {
            btnScreenBack.setOnClickListener {
                findNavController().popBackStack()
            }
            listCategories.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = BookCategoriesListAdapter(handleBookCategoriesListItemClicked)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(), DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }

    private val handleBookCategoriesListItemClicked: (BookCategory) -> Unit = { category ->
        Timber.d("handleBookCategoriesListItemClicked: $category")
        Toast.makeText(requireContext(), "Selected category: $category", Toast.LENGTH_LONG).show()
    }

}