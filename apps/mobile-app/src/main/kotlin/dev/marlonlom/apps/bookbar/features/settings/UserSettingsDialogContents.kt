/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.features.settings

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Public
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.marlonlom.apps.bookbar.R

@Composable
internal fun SettingsPanelContent(
    settingsUiState: SettingsUiState.Success,
    onBooleanSettingUpdated: (String, Boolean) -> Unit
) {
  BooleanSettingSwitchRow(
    titleTextRes = R.string.text_settings_dynamic_color_preference,
    booleanSettingKey = "dynamic_colors",
    booleanSettingValue = settingsUiState.settings.useDynamicColor,
    onBooleanSettingUpdated = onBooleanSettingUpdated,
    switchEnabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
  )
  BooleanSettingSwitchRow(
    titleTextRes = R.string.text_settings_dark_theme_preference,
    booleanSettingKey = "dark_theme",
    booleanSettingValue = settingsUiState.settings.useDarkTheme,
    onBooleanSettingUpdated = onBooleanSettingUpdated,
    switchEnabled = !isSystemInDarkTheme()
  )
}

@Composable
private fun BooleanSettingSwitchRow(
  @StringRes titleTextRes: Int,
  booleanSettingKey: String,
  booleanSettingValue: Boolean,
  onBooleanSettingUpdated: (String, Boolean) -> Unit,
  switchEnabled: Boolean = true
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 10.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = stringResource(titleTextRes),
      style = MaterialTheme.typography.titleMedium,
    )
    Switch(
      enabled = switchEnabled,
      checked = booleanSettingValue,
      onCheckedChange = { checked ->
        onBooleanSettingUpdated(booleanSettingKey, checked)
      },
    )
  }
}

@Composable
internal fun SettingsSectionDivider() {
  Divider(Modifier.padding(top = 8.dp))
}

@ExperimentalLayoutApi
@Composable
fun LinksPanelContent(
  openExternalUrl: (String) -> Unit,
  openOssLicencesInfo: () -> Unit,
) {
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(
      space = 16.dp,
      alignment = Alignment.CenterHorizontally,
    ),
    modifier = Modifier.fillMaxWidth(),
  ) {
    val privacyPolicyUrl = stringResource(R.string.url_settings_privacy_policy)
    val feedbackUrl = stringResource(R.string.url_settings_feedback)
    TextButton(onClick = { openExternalUrl(privacyPolicyUrl) }) {
      Text(text = stringResource(R.string.text_settings_privacy_policy))
    }
    TextButton(onClick = { openExternalUrl(feedbackUrl) }) {
      Text(text = stringResource(R.string.text_settings_feedback))
    }
    TextButton(onClick = { openOssLicencesInfo() }) {
      Text(text = stringResource(R.string.text_settings_licenses))
    }
  }
}


@Composable
internal fun AboutItBookstoreText(
  openExternalUrl: (String) -> Unit
) {
  val urlAboutItBookstore = stringResource(id = R.string.url_settings_about_itbookstore)
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 10.dp, vertical = 4.dp)
      .padding(top = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
      Text(
        text = stringResource(id = R.string.title_settings_about_itbookstore),
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        style = MaterialTheme.typography.bodyLarge
      )
      Text(
        text = stringResource(id = R.string.text_settings_about_itbookstore),
        style = MaterialTheme.typography.bodyMedium
      )
    }
    Column {
      IconButton(
        onClick = { openExternalUrl(urlAboutItBookstore) },
      ) {
        Icon(
          Icons.TwoTone.Public,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}
