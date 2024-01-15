/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/**
 * Bookbar book sharing single object utility.
 *
 * @author marlonlom
 */
object BookSharingUtil {

  /**
   * Executes share intent using the provided message text.
   *
   * @param context Application context.
   * @param message Message as text.
   *
   */
  fun beginShare(
    context: Context,
    message: String,
  ) {
    Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "Bookbar")
      putExtra(Intent.EXTRA_TEXT, message)
    }.apply {
      ContextCompat.startActivity(
        /* context = */ context,
        /* intent = */ Intent.createChooser(this, "Share"),
        /* options = */ null
      )
    }
  }
}
