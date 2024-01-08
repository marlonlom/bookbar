/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Main application navigation rail composable ui.
 *
 * @author marlonlom
 *
 * @param navSelectedIndex
 * @param onNavSelectedIndexChanged
 */
@Composable
fun MainNavigationRail(
  navSelectedIndex: Int,
  onNavSelectedIndexChanged: (Int, String) -> Unit,
) {
  NavigationRail {
    BookbarRoutes.bottomNavRoutes.forEachIndexed { index, tuple ->
      NavigationRailItem(
        selected = navSelectedIndex == index,
        onClick = { onNavSelectedIndexChanged(index, tuple.route) },
        icon = {
          Icon(
            imageVector = tuple.icon!!,
            contentDescription = tuple.route
          )
        },
        label = {
          Text(text = stringResource(id = tuple.label))
        }
      )
    }
  }
}
