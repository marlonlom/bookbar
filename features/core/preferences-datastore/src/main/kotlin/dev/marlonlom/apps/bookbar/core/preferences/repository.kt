/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * User preferences repository class.
 *
 * @author marlonlom
 *
 * @property dataStore Preferences datastore.
 */
class UserPreferencesRepository(
  private val dataStore: DataStore<Preferences>,
) {

  private object UserPreferenceKeys {

    private val booleanKeys = arrayOf("dark_theme", "dynamic_colors")

    val DARK_THEME = booleanPreferencesKey(booleanKeys[0])
    val DYNAMIC_COLORS = booleanPreferencesKey(booleanKeys[1])

    fun getBooleanPref(key: String) = when (booleanKeys.indexOf(key)) {
      0 -> DARK_THEME
      1 -> DYNAMIC_COLORS
      else -> null
    }
  }

  /** User preferences information as Flow. */
  val userPreferencesFlow: Flow<UserSettings> = dataStore.data
    .catch { throwable ->
      if (throwable is IOException) {
        emit(emptyPreferences())
      } else {
        throw throwable
      }
    }
    .map { prefs ->
      val useDarkTheme = prefs[UserPreferenceKeys.DARK_THEME] ?: false
      val useDynamicColors = prefs[UserPreferenceKeys.DYNAMIC_COLORS] ?: true
      UserSettings(useDarkTheme, useDynamicColors)
    }

  /**
   * Toggles the boolean value for selected setting key.
   *
   * @param booleanKey Boolean preference key.
   * @param newBooleanValue Boolean preference value for update.
   */
  suspend fun toggleBooleanSetting(booleanKey: String, newBooleanValue: Boolean) {
    dataStore.edit { preferences ->
      val booleanPref = UserPreferenceKeys.getBooleanPref(booleanKey)
      if (booleanPref != null) {
        preferences[booleanPref] = newBooleanValue
      }
    }
  }

}
