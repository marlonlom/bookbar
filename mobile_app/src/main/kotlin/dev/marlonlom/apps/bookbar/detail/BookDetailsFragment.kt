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

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.browser.customtabs.CustomTabsIntent
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
import kotlinx.coroutines.Job
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

    private lateinit var foundBookJob: Job
    private lateinit var savedBookFlagJob: Job

    private fun newViewModelFactory(): BookDetailsContract.ViewModelFactory {
        Timber.w("Getting new ViewModel using network api and local database for book detail listing ...")
        val apiService = BookStoreApi.Service.newService(BuildConfig.API_BASE_URL.toHttpUrl())
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val localDataSource = BookDetailsContract.LocalDataSource(appDatabase)
        val remoteDataSource = BookDetailsContract.RemoteDataSource(apiService)
        val repository = BookDetailsContract.Repository(localDataSource, remoteDataSource)
        return BookDetailsContract.ViewModelFactory(repository)
    }

    override fun onStart() {
        super.onStart()
        savedBookFlagJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            Timber.i("launchWhenStarted >> obtaining book saved flag")
            uiViewModel.bookSaved.collect { isBookSaved ->
                handleSavedBookFlag(isBookSaved)
            }
        }
        foundBookJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            Timber.i("launchWhenStarted >> obtaining book detail using isbn")
            uiViewModel.book.collect { book ->
                val isOtherThanNone = book != BookDetail.NONE
                Timber.d("isOtherThanNone=$isOtherThanNone")
                if (isOtherThanNone) {
                    processFoundBook(book)
                }
            }
        }
    }

    override fun onStop() {
        savedBookFlagJob.cancel()
        foundBookJob.cancel()
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        handleOnBackPressed()
        setupScreen()
        obtainBookInformation()
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackNavigation()
        }
    }

    private fun obtainBookInformation() {
        Timber.d("obtaining book information for isbn=${args.bookIsbn}")
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
                handleBackNavigation()
            }
            btnBookSave.setOnClickListener {
                uiViewModel.toggleSaved(args.bookIsbn)
            }
        }
    }

    private fun handleBackNavigation() {
        clearScreenContents()
        findNavController().popBackStack()
    }

    private fun clearScreenContents() {
        uiBinding.apply {
            imageDetailPoster.load(R.drawable.img_book_placeholder) {
                crossfade(true)
            }
            textDetailTitle.text = ""
            textDetailSubtitle.text = ""
            textDetailAuthors.text = ""
            textDetailPrice.text = ""
            textDetailPublisher.text = ""
            textDetailPublished.text = ""
            textDetailPages.text = ""
            textDetailLanguage.text = ""
            textDetailIsbn10.text = ""
            textDetailIsbn13.text = ""
            textDetailDescription.text = ""
            detailRatingValue.rating = 0.toFloat()

            btnBookBuyOrDownload.visibility = View.GONE

            uiViewModel.clearState()
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
            setupBookBuyOrDownloadButton(book)
            btnBookShare.setOnClickListener {
                performBookSharing(book)
            }
        }
    }

    private fun setupBookBuyOrDownloadButton(book: BookDetail) {
        Timber.d("setupBookBuyOrDownloadButton")
        uiBinding.btnBookBuyOrDownload.apply {
            val selectedBtnText = when (book.isFree) {
                true -> R.string.btn_label_detail_download_book
                else -> R.string.btn_label_detail_buy_book
            }
            val selectedBtnIcon = when (book.isFree) {
                true -> R.drawable.ic_detail_download_free
                else -> R.drawable.ic_detail_buy
            }
            text = requireContext().getString(selectedBtnText)
            icon = ResourcesCompat.getDrawable(resources, selectedBtnIcon, null)
            setOnClickListener {
                handleBookBuyOrDownloadClick(book)
            }
            visibility = View.VISIBLE
        }
    }

    private fun handleBookBuyOrDownloadClick(book: BookDetail) {
        val isFreeEbook = book.isFree
        Timber.d("handleBookBuyOrDownloadClick ... book.isFree=$isFreeEbook")
        if (!isFreeEbook!!) {
            val buyUrl = requireContext().getString(R.string.url_detail_buy_book, book.isbn13)
            Timber.d("handle external navigation for buying process: \"$buyUrl\"")
            val customTab = CustomTabsIntent.Builder().build()
            customTab.launchUrl(requireContext(), Uri.parse(buyUrl))
        } else {
            Timber.d("handle external navigation for download free pdf process: \"${book.freePdfUrl}\" ")
        }
    }

    private fun performBookSharing(book: BookDetail) {
        Timber.d("performBookSharing")
    }

}