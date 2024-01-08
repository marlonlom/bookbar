/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dev.marlonlom.demos.bookbar.R

/**
 * Bookbar app navigation routes sealed class.
 *
 * @author marlonlom
 *
 * @property route Route name.
 * @property label Route label string resource.
 * @property icon Route icon as image vector.
 */
sealed class BookbarRoutes(
  val route: String,
  @StringRes
  val label: Int = -1,
  val icon: ImageVector? = null
) {

  /**
   * Home destination for application navigation route.
   *
   * @author marlonlom
   */
  data object Home : BookbarRoutes(
    route = "home",
    label = R.string.text_route_home,
    icon = Icons.TwoTone.Home
  )

  /**
   * Favorite books destination for application navigation route.
   *
   * @author marlonlom
   */
  data object Favorite : BookbarRoutes(
    route = "favorite",
    label = R.string.text_route_favorite,
    icon = Icons.TwoTone.Favorite
  )

  /**
   * User settings destination for application navigation route.
   *
   * @author marlonlom
   */
  data object Preferences : BookbarRoutes(
    route = "settings",
    label = R.string.text_route_settings,
    icon = Icons.TwoTone.Settings
  )

  companion object {

    /** Bottom navigation routes list. */
    @JvmStatic
    val bottomNavRoutes = arrayListOf(
      Home, Favorite, Preferences
    )
  }
}
