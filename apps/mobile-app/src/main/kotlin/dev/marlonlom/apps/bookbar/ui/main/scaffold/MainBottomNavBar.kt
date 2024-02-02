/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Main bottom navigation composable ui.
 *
 * @author marlonlom
 *
 * @param navSelectedIndex
 * @param onNavSelectedIndexChanged
 */
@Composable
fun MainBottomNavBar(
  navSelectedIndex: Int,
  onNavSelectedIndexChanged: (Int, String) -> Unit
) {
  NavigationBar {
    bottomNavRoutes.forEachIndexed { index, tuple ->
      NavigationBarItem(
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
