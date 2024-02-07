package dev.marlonlom.apps.bookbar.features.book_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.marlonlom.apps.bookbar.domain.books.BookDetailItem
import dev.marlonlom.apps.bookbar.domain.books.BookDetailResult
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState

/**
 * @author marlonlom
 *
 * @param appState
 * @param onBackNavigationIconClicked
 * @param onBuyBookIconClicked
 * @param onReadMoreTextClicked
 * @param onFavoriteBookIconClicked
 * @param onShareIconClicked
 */
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun BookDetailStatusContent(
  appState: BookbarAppState,
  onBackNavigationIconClicked: () -> Unit,
  onBuyBookIconClicked: (String) -> Unit,
  onReadMoreTextClicked: (String) -> Unit,
  onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
  onShareIconClicked: (String) -> Unit,
) =
  when (appState.bookDetails) {
    BookDetailResult.Loading -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Loading book ...", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
      }
    }

    BookDetailResult.NotFound -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = appState.bookDetails.toString(), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
      }
    }

    is BookDetailResult.Success -> {
      BookDetailContent(
        appState = appState,
        bookDetailItem = appState.bookDetails.item,
        onBackNavigationIconClicked = onBackNavigationIconClicked,
        onBuyBookIconClicked = onBuyBookIconClicked,
        onReadMoreTextClicked = onReadMoreTextClicked,
        onFavoriteBookIconClicked = onFavoriteBookIconClicked,
        onShareIconClicked = onShareIconClicked,
      )
    }
  }
