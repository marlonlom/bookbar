/*
 * Copyright (c) 2021 marlonlom.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.marlonlom.apps.bookbar.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.utils.ThemeManager
import timber.log.Timber

/**
 * Application settings fragment class.
 *
 * @author marlonlom
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.bookbar_preferences, rootKey)
        setupThemeSelectionList(findPreference("preference_key_dark_theme")!!)
    }

    private fun setupThemeSelectionList(themePreference: ListPreference) {
        Timber.d("setupThemeSelectionList")
        themePreference.setDefaultValue(
            themePreference.sharedPreferences.getString(
                "pref_theme_value",
                "default"
            )
        )
        themePreference.setOnPreferenceChangeListener { preference, newValue ->
            Timber.d("themePreference.setOnPreferenceChangeListener(${preference.key}, $newValue)")
            val themeModesArray =
                requireContext().resources.getStringArray(R.array.entry_values_for_dark_theme_setting)
            val selectedDefaultValue =
                ThemeManager.getSelectedDefaultValue(themeModesArray, newValue.toString())
            val selectedUiMode =
                ThemeManager.getSelectedUiMode(themeModesArray, newValue.toString())

            preference.preferenceManager.sharedPreferences.edit(commit = true) {
                putString("pref_theme_value", selectedDefaultValue)
            }
            Timber.d("Values(selectedDefaultValue=$selectedDefaultValue, selectedUiMode=$selectedUiMode)")
            preference.setDefaultValue(selectedDefaultValue)

            AppCompatDelegate.setDefaultNightMode(selectedUiMode)
            requireActivity().recreate()
            true
        }
    }
}
