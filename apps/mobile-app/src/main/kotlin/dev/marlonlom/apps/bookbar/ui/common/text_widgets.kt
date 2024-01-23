/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.ui.main.BookbarAppState

/**
 * Welcome title text common composable ui.
 *
 * @author marlonlom
 *
 * @param appState Application ui state.
 */
@Composable
fun WelcomeTitle(
  appState: BookbarAppState
) {
  val topBaselinePadding = when {
    appState.is7InTabletWidth and appState.isLandscapeOrientation -> 40.dp
    appState.is7InTabletWidth -> 80.dp
    appState.is10InTabletWidth -> 100.dp
    appState.isCompactHeight -> 40.dp
    else -> 60.dp
  }

  val textStyle = when {
    appState.is7InTabletWidth and appState.isLandscapeOrientation -> MaterialTheme.typography.titleMedium
    appState.is7InTabletWidth -> MaterialTheme.typography.headlineMedium
    else -> MaterialTheme.typography.titleMedium
  }

  Text(
    modifier = Modifier
      .fillMaxWidth()
      .paddingFromBaseline(top = topBaselinePadding),
    text = stringResource(R.string.text_general_welcome),
    style = textStyle,
    color = MaterialTheme.colorScheme.onBackground,
    fontWeight = FontWeight.Normal
  )
}

/**
 * Headline title text common composable ui.
 *
 * @param appState Application ui state.
 * @param headlineTextRes Headline text as string resource.
 */
@Composable
fun HeadlineTitle(
  appState: BookbarAppState,
  @StringRes headlineTextRes: Int
) {
  val textStyle = when {
    appState.is7InTabletWidth and appState.isLandscapeOrientation -> MaterialTheme.typography.displaySmall
    appState.is7InTabletWidth -> MaterialTheme.typography.displayLarge
    else -> MaterialTheme.typography.displaySmall
  }

  Text(
    modifier = Modifier
      .fillMaxWidth()
      .paddingFromBaseline(top = 40.sp, bottom = 20.sp),
    text = stringResource(headlineTextRes),
    style = textStyle,
    color = MaterialTheme.colorScheme.onBackground,
    fontWeight = FontWeight.Bold
  )
}
