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

package dev.marlonlom.apps.bookbar

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dev.marlonlom.apps.bookbar.utils.ThemeManager
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Bookbar Application class.
 *
 * @author marlonlom
 */
class BookbarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        handleDefaultNightMode()
    }

    private fun handleDefaultNightMode() {
        Timber.d("handleDefaultNightMode")
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themeDefaultValue = defaultSharedPreferences.getString("pref_theme_value", "default")
        val themeModesArray = resources.getStringArray(R.array.entry_values_for_dark_theme_setting)
        val selectedUiMode = ThemeManager.getSelectedUiMode(themeModesArray, themeDefaultValue!!)
        AppCompatDelegate.setDefaultNightMode(selectedUiMode)
    }
}