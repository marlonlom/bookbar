/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.marlonlom.apps.bookbar.core.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * User preferences viewmodel.
 *
 * @author marlonlom
 *
 * @property repository User preferences repository.
 */
class UserSettingsViewModel(
  private val repository: UserPreferencesRepository
) : ViewModel() {

  /** User preferences ui state as Flow. */
  val settingsUiState: StateFlow<SettingsUiState> = repository.userPreferencesFlow
    .map {
      SettingsUiState.Success(
        UserEditableSettings(
          useDarkTheme = it.useDarkTheme,
          useDynamicColor = it.useDynamicColor
        )
      )
    }.stateIn(
      scope = viewModelScope,
      started = WhileSubscribed(5.seconds.inWholeMilliseconds),
      initialValue = SettingsUiState.Loading,
    )

  /**
   * Toggles the boolean value for selected setting key.
   *
   * @param key Boolean preference key.
   * @param booleanValue Boolean preference value for update.
   */
  fun updateBooleanInfo(key: String, booleanValue: Boolean) {
    viewModelScope.launch {
      repository.toggleBooleanSetting(key, booleanValue)
    }
  }

}

/**
 * User editable settings data class.
 *
 * @author marlonlom
 *
 * @property useDarkTheme True/False if using dark theme.
 * @property useDynamicColor True/False if using dynamic colors.
 */
data class UserEditableSettings(
  val useDarkTheme: Boolean,
  val useDynamicColor: Boolean,
)

/**
 * Settings ui state sealed interface definition.
 *
 * @author marlonlom
 */
sealed interface SettingsUiState {

  /**
   * Loading phase of settings ui state.
   *
   * @author marlonlom
   */
  data object Loading : SettingsUiState

  /**
   * Loading phase of settings ui state.
   *
   * @author marlonlom
   *
   * @property settings User editable settings information.
   */
  data class Success(
    val settings: UserEditableSettings
  ) : SettingsUiState
}
