/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.preferences

/**
 * User settings data class.
 *
 * @author marlonlom
 *
 * @property useDarkTheme True/False if dark theme is set.
 * @property useDynamicColor True/False if dynamic colors are set.
 */
data class UserSettings(
  val useDarkTheme: Boolean,
  val useDynamicColor: Boolean,
)
