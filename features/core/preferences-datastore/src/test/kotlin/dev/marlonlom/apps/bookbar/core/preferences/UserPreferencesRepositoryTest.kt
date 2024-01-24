/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class UserPreferencesRepositoryTest {

  private lateinit var repository: UserPreferencesRepository

  @Before
  fun begin() {
    repository = UserPreferencesRepository(FakeDataStore)
  }

  @Test
  fun `Should return settings as flow`() = runBlocking {
    val settings = repository.userPreferencesFlow.first()
    assertNotNull(settings)
    assertFalse(settings.useDarkTheme)
    assertTrue(settings.useDynamicColor)
  }

}

internal object FakeDataStore : DataStore<Preferences> {

  private val emptyPreferences = emptyPreferences()

  override val data: Flow<Preferences>
    get() {
      return flowOf(emptyPreferences)
    }

  override suspend fun updateData(
    transform: suspend (t: Preferences) -> Preferences
  ): Preferences =
    transform(emptyPreferences.toMutablePreferences())

}
