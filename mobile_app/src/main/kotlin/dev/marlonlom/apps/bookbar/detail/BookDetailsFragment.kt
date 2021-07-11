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

package dev.marlonlom.apps.bookbar.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import coil.load
import dev.marlonlom.apps.bookbar.BuildConfig
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.databinding.FragmentBookDetailsBinding
import dev.marlonlom.apps.bookbar.model.database.AppDatabase
import dev.marlonlom.apps.bookbar.model.database.book_detail.BookDetail
import dev.marlonlom.apps.bookbar.model.network.BookStoreApi
import dev.marlonlom.apps.bookbar.viewbindings.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber

/**
 * Book details fragment class.
 *
 * @author marlonlom
 */
@ExperimentalSerializationApi
class BookDetailsFragment : Fragment(R.layout.fragment_book_details) {

    private val uiBinding by viewBinding(FragmentBookDetailsBinding::bind)
    private val uiViewModel: BookDetailsContract.ViewModel by navGraphViewModels(R.id.navigation_bookstore) { newViewModelFactory() }
    private val args: BookDetailsFragmentArgs by navArgs()

    private fun newViewModelFactory(): BookDetailsContract.ViewModelFactory {
        Timber.w("Getting new ViewModel using network api and local database for book detail listing ...")
        val apiService = BookStoreApi.Service.newService(BuildConfig.API_BASE_URL.toHttpUrl())
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val localDataSource = BookDetailsContract.LocalDataSource(appDatabase)
        val remoteDataSource = BookDetailsContract.RemoteDataSource(apiService)
        val repository = BookDetailsContract.Repository(localDataSource, remoteDataSource)
        return BookDetailsContract.ViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        setupScreen()
        obtainReleasedBooks()
    }

    private fun obtainReleasedBooks() {
        Timber.d("obtainReleasedBooks")
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            Timber.i("launchWhenStarted >> obtaining book detail using isbn")
            uiViewModel.bookSaved.collect { isBookSaved ->
                handleSavedBookFlag(isBookSaved)
            }
            uiViewModel.book.collect { book ->
                if (book != BookDetail.NONE) {
                    processFoundBook(book)
                }
            }
        }
        uiViewModel.retrieveBook(args.bookIsbn)
    }

    private fun handleSavedBookFlag(isBookSaved: Boolean) {
        Timber.d("handleSavedBookFlag (isBookSaved=$isBookSaved)")
        val iconDrawable = when (isBookSaved) {
            true -> R.drawable.ic_detail_bookmarked
            else -> R.drawable.ic_detail_bookmark
        }
        uiBinding.btnBookSave.apply {
            icon =
                ResourcesCompat.getDrawable(
                    requireContext().resources, iconDrawable, null
                )
            invalidate()
        }
    }

    private fun setupScreen() {
        Timber.d("setupScreen")
        uiBinding.apply {
            btnScreenBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnBookSave.setOnClickListener {
                uiViewModel.toggleSaved(args.bookIsbn)
            }
        }
    }

    private fun processFoundBook(book: BookDetail) {
        Timber.d("processFoundBook")
        uiBinding.apply {
            imageDetailPoster.load(book.image) {
                crossfade(true)
                placeholder(R.drawable.img_book_placeholder)
            }
            textDetailTitle.text = book.title
            textDetailSubtitle.text = book.subtitle
            textDetailAuthors.text = book.authors
            textDetailPrice.text = book.price
            textDetailPublisher.text = book.publisher
            textDetailPublished.text = book.year
            textDetailPages.text = book.pages
            textDetailLanguage.text = book.language
            textDetailIsbn10.text = book.isbn10
            textDetailIsbn13.text = book.isbn13
            textDetailDescription.text = book.desc
            detailRatingValue.rating = book.rating.let { if (it.isEmpty()) "0" else it }.toFloat()

            btnBookShare.setOnClickListener {
                performBookSharing(book)
            }
        }
    }

    private fun performBookSharing(book: BookDetail) {
        Timber.d("performBookSharing")
    }

}