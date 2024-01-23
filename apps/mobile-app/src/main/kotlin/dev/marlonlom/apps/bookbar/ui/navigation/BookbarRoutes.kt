/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.MenuBook
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.marlonlom.apps.bookbar.R

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
    icon = Icons.TwoTone.MenuBook
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

  /**
   * Book details destination for application navigation route.
   *
   * @author marlonlom
   */
  data object Details : BookbarRoutes(
    route = "book/{bookId}",
  ) {

    /** Navigation arguments for detail route. */
    val navArguments get() = listOf(navArgument("bookId") { type = NavType.StringType })

    /**
     * Creates a route for getting book details.
     *
     * @param bookId Book id as isbn13.
     *
     * @return Book details route text.
     */
    fun createRoute(bookId: String) = "book/${bookId}"
  }
}

val bottomNavRoutes: List<BookbarRoutes> = listOf(BookbarRoutes.Home, BookbarRoutes.Favorite, BookbarRoutes.Preferences)
