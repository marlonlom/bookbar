/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.main

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dev.marlonlom.apps.bookbar.domain.books.BookDetailItem
import dev.marlonlom.apps.bookbar.ui.features.book_detail.BookDetailsViewModel
import dev.marlonlom.apps.bookbar.ui.features.books_favorite.FavoriteBooksViewModel
import dev.marlonlom.apps.bookbar.ui.util.BookSharingUtil
import dev.marlonlom.apps.bookbar.ui.util.CustomTabsOpener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber


/**
 * App content callbacks data class.
 *
 * @author marlonlom
 *
 * @property openOssLicencesInfo Action for opening oss licences information window.
 * @property openExternalUrl Action for opening oss licences information window.
 * @property onShareIconClicked Action for performing sharing functionality.
 * @property onBookSelectedForDetail Action for selecting book for details.
 * @property onFavoriteBookIconClicked Action for favorite book button clicked.
 * @property onRemoveFavoriteIconClicked
 */
data class AppContentCallbacks(
  val openOssLicencesInfo: () -> Unit,
  val openExternalUrl: (String) -> Unit,
  val onShareIconClicked: (String) -> Unit,
  val onBookSelectedForDetail: (String) -> Unit,
  val onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
  val onRemoveFavoriteIconClicked: (String) -> Unit
)

/**
 * Creates new app content callbacks object.
 *
 * @author marlonlom
 *
 * @param activityContext Activity context.
 * @param favoriteBooksViewModel Favorite Book listing viewmodel.
 * @param bookDetailsViewModel Book details viewmodel.
 */
@ExperimentalCoroutinesApi
@Composable
internal fun newAppContentCallbacks(
  activityContext: Context,
  favoriteBooksViewModel: FavoriteBooksViewModel,
  bookDetailsViewModel: BookDetailsViewModel
) = AppContentCallbacks(
  openOssLicencesInfo = {
    Timber.d("[AppContent.openOssLicencesInfo] Should open oss licences information content.")
    activityContext.startActivity(Intent(activityContext, OssLicensesMenuActivity::class.java))
  },
  openExternalUrl = { externalUrl ->
    Timber.d("[AppContent.openExternalUrl] Should open external url '$externalUrl'.")
    CustomTabsOpener.openUrl(activityContext, externalUrl)
  },
  onShareIconClicked = { message ->
    Timber.d("[AppContent.onShareIconClicked] Should share a book.")
    BookSharingUtil.beginShare(activityContext, message)
  },
  onBookSelectedForDetail = { bookId ->
    Timber.d("[AppContent.onBookSelectedForDetail] Should select book for details.")
    bookDetailsViewModel.setSelectedBook(bookId = bookId)
  },
  onFavoriteBookIconClicked = { bookDetail: BookDetailItem, markedFavorite: Boolean ->
    Timber.d("[AppContent.onSaveFavoriteBook] Should toggle Book[${bookDetail.isbn13}] as favorite? $markedFavorite.")
    bookDetailsViewModel.toggleFavorite(bookDetail, markedFavorite)
  },
  onRemoveFavoriteIconClicked = { bookId ->
    Timber.d("[AppContent.onRemoveFavoriteIconClicked] Should remove Book[$bookId] as favorite.")
    favoriteBooksViewModel.removeFavorite(bookId)
  }
)
