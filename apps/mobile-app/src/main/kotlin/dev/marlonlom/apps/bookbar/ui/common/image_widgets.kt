/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.marlonlom.apps.bookbar.R

/**
 * Book poster image composable ui.
 *
 * @author marlonlom
 *
 * @param bookTitle Book title.
 * @param bookPosterImage Book title.
 * @param imageHeight Image height.
 * @param aspectRatio Image aspect ratio
 */
@Composable
fun BookPosterImage(
  bookTitle: String,
  bookPosterImage: String,
  imageHeight: Dp,
  aspectRatio: Float = 6f / 7f
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(bookPosterImage)
      .crossfade(true)
      .build(),
    placeholder = painterResource(R.drawable.img_books_placeholder),
    contentDescription = bookTitle,
    contentScale = ContentScale.Crop,
    modifier = Modifier
      .clip(RoundedCornerShape(10.dp))
      .height(imageHeight)
      .aspectRatio(aspectRatio)
  )
}
