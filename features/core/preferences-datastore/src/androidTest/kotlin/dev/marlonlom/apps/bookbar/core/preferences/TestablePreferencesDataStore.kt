/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope

object TestablePreferencesDataStore {
  private var instance: DataStore<Preferences>? = null

  fun getInstance(testCoroutineScope: CoroutineScope): DataStore<Preferences> {
    return instance ?: synchronized(this) {
      instance ?: PreferenceDataStoreFactory.create(
        scope = testCoroutineScope,
        produceFile = {
          val testContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
          testContext.preferencesDataStoreFile(name = "test_bookbar_settings")
        }
      ).also { instance = it }
    }
  }
}
