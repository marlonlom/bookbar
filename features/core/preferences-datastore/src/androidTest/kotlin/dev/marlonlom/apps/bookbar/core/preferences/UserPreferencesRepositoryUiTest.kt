/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserPreferencesRepositoryUiTest {

  private val testCoroutineDispatcher = UnconfinedTestDispatcher()
  private val testCoroutineScope = TestScope(testCoroutineDispatcher)

  private var repository: UserPreferencesRepository? = null

  @Before
  fun setUp() {
    repository = UserPreferencesRepository(
      TestablePreferencesDataStore.getInstance(testCoroutineScope)
    )
  }

  @After
  fun tearDown() {
    testCoroutineScope.cancel()
    repository = null
  }

  @Test
  fun repository_testFetchInitialPreferences() {
    testCoroutineScope.launch {
      val preferences = repository!!.userPreferencesFlow.first()
      with(preferences) {
        assertThat(this.useDynamicColor).isTrue()
        assertThat(this.useDarkTheme).isFalse()
      }
    }
  }

  @Test
  fun repository_toggleBooleanSettings() {
    testCoroutineScope.launch {
      repository!!.toggleBooleanSetting("dark_theme", true)
      repository!!.toggleBooleanSetting("dynamic_colors", false)
      val preferences = repository!!.userPreferencesFlow.first()
      with(preferences) {
        assertThat(this.useDarkTheme).isTrue()
        assertThat(this.useDynamicColor).isFalse()
      }
    }
  }

}
