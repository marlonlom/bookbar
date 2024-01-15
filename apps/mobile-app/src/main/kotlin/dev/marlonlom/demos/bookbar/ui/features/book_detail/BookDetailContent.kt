/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.book_detail

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.marlonlom.demos.bookbar.R
import dev.marlonlom.demos.bookbar.domain.books.BookDetailItem
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState
import timber.log.Timber

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun BookDetailContent(
  appState: BookbarAppState,
  bookDetailItem: BookDetailItem,
  onBackNavigationIconClicked: () -> Unit,
  onBuyBookIconClicked: (String) -> Unit,
  onReadMoreTextClicked: (String) -> Unit,
  onFavoriteBookIconClicked: (BookDetailItem, Boolean) -> Unit,
  onShareIconClicked: (String) -> Unit,
  backgroundColor: Color = MaterialTheme.colorScheme.background
) {
  LazyColumn(
    state = rememberLazyListState(),
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    stickyHeader {
      HeaderTopBar(
        appState = appState,
        bookDetailItem = bookDetailItem,
        backgroundColor = backgroundColor,
        onBackNavigationIconClicked = onBackNavigationIconClicked,
        onFavoriteBookIconClicked = onFavoriteBookIconClicked,
        onShareBookIconClicked = onShareIconClicked
      )
    }
    item {
      BookHeadingSection(
        appState = appState,
        bookDetailItem = bookDetailItem,
        onBuyBookIconClicked = onBuyBookIconClicked
      )
      BookDetailDivider()
    }

    if (appState.isLandscapeOrientation.and(appState.is10InTabletWidth)) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(0.9f),
          horizontalArrangement = Arrangement.spacedBy(30.dp),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            BookTextSlots(bookDetailItem, onReadMoreTextClicked)
          }
          Column(
            modifier = Modifier.padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            BookInfoTableCells(bookDetailItem)
          }
        }
      }
    } else {
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          BookInfoTableCells(bookDetailItem)
        }
        BookDetailDivider()
      }
      item {
        BookTextSlots(bookDetailItem, onReadMoreTextClicked)
      }
    }
  }
}

@Composable
internal fun BookTextSlots(
  bookDetailItem: BookDetailItem,
  onReadMoreTextClicked: (String) -> Unit
) {
  BookTextSlot(
    titleStringRes = R.string.text_detail_book_authors,
    sectionContent = {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 10.dp),
        text = bookDetailItem.authors,
        style = MaterialTheme.typography.bodyLarge,
      )
    }
  )
  BookTextSlot(
    titleStringRes = R.string.text_detail_book_description,
    sectionContent = {
      val readMoreText = stringResource(R.string.text_detail_read_more)
      val annotatedString = buildAnnotatedString {
        append(bookDetailItem.desc.replace("&#039;","'"))
        append("  ")
        pushStringAnnotation(tag = readMoreText, annotation = bookDetailItem.url)
        withStyle(
          SpanStyle(
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
          )
        ) {
          append(readMoreText)
        }
        pop()
      }

      ClickableText(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 40.dp),
        text = annotatedString,
        style = TextStyle(
          fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
          fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
          fontSize = MaterialTheme.typography.bodyLarge.fontSize,
          lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
          color = MaterialTheme.colorScheme.onBackground
        ),
        onClick = { offset ->
          annotatedString.getStringAnnotations(
            tag = readMoreText, start = offset, end = offset
          ).firstOrNull()?.let {
            Timber.d("Read more about book[${bookDetailItem.isbn13}] using url: ${it.item}")
            onReadMoreTextClicked(it.item)
          }
        }
      )
    }
  )
}

@Composable
internal fun BookInfoTableCells(bookDetailItem: BookDetailItem) {
  BookInfoTableCell(R.string.text_detail_book_isbn10, bookDetailItem.isbn10)
  BookInfoTableCell(R.string.text_detail_book_isbn13, bookDetailItem.isbn13)
  BookInfoTableCell(R.string.text_detail_book_publisher, bookDetailItem.publisher)
  BookInfoTableCell(R.string.text_detail_book_published_year, bookDetailItem.year)
  BookInfoTableCell(R.string.text_detail_book_pages, bookDetailItem.pages)
  BookInfoTableCell(R.string.text_detail_book_language, bookDetailItem.language)
}

@Composable
internal fun BookInfoTableCell(
  @StringRes titleStringRes: Int,
  detailText: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .paddingFromBaseline(top = 20.dp, bottom = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(Modifier.fillMaxWidth(0.4f)) {
      Text(
        text = stringResource(titleStringRes),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
      )
    }
    Column {
      Text(
        text = detailText,
        style = MaterialTheme.typography.bodyLarge
      )
    }
  }
}
