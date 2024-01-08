/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.features.books_favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.marlonlom.demos.bookbar.R
import dev.marlonlom.demos.bookbar.ui.common.HeadlineTitle
import dev.marlonlom.demos.bookbar.ui.common.WelcomeTitle
import dev.marlonlom.demos.bookbar.ui.main.BookbarAppState

@Composable
fun FavoriteBooksRoute(appState: BookbarAppState) {
  val contentHorizontalPadding = if (appState.is10InTabletWidth) 80.dp else 20.dp
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = contentHorizontalPadding)
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    WelcomeTitle(appState = appState)
    HeadlineTitle(appState = appState, headlineTextRes = R.string.title_bottom_favorites)
    LazyVerticalGrid(
      columns = GridCells.Fixed(3),
      content = {

      }
    )
  }
}

