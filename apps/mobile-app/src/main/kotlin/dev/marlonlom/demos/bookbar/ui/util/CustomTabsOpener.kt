/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

/**
 * Chrome custom tabs opener single object utility.
 *
 * @author marlonlom
 *
 */
object CustomTabsOpener {

  /**
   * Opens an url as a custom tab.
   *
   * @param context Application context.
   * @param url External url.
   */
  fun openUrl(context: Context, url: String) {
    val builder = CustomTabsIntent.Builder()
      .setShowTitle(true)
      .setInstantAppsEnabled(true)
      .setDefaultColorSchemeParams(
        CustomTabColorSchemeParams.Builder().build()
      ).build()

    builder.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    builder.launchUrl(context, Uri.parse(url))
  }

}
