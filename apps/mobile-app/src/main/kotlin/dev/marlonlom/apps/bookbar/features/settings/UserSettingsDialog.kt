/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.marlonlom.apps.bookbar.R
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun UserSettingsDialog(
  appState: BookbarAppState,
  viewModel: UserSettingsViewModel,
  onDialogDismissed: () -> Unit,
  openOssLicencesInfo: () -> Unit,
  openExternalUrl: (String) -> Unit,
) {
  val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
  UserSettingsDialog(
    appState = appState,
    settingsUiState = settingsUiState,
    onDialogDismissed = onDialogDismissed,
    onBooleanSettingUpdated = viewModel::updateBooleanInfo,
    openOssLicencesInfo = openOssLicencesInfo,
    openExternalUrl = openExternalUrl
  )
}

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
private fun UserSettingsDialog(
  appState: BookbarAppState,
  settingsUiState: SettingsUiState,
  onDialogDismissed: () -> Unit,
  onBooleanSettingUpdated: (String, Boolean) -> Unit,
  openExternalUrl: (String) -> Unit,
  openOssLicencesInfo: () -> Unit,
) {
  val configuration = LocalConfiguration.current
  AlertDialog(
    modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
    ),
    onDismissRequest = {
      onDialogDismissed()
    },
    title = {
      Text(
        text = stringResource(id = R.string.text_route_settings),
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleLarge,
      )
    },
    text = {
      Divider()
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        when (settingsUiState) {
          SettingsUiState.Loading -> {
            Row(
              modifier = Modifier
                .size(56.dp)
                .padding(10.dp)
                .padding(vertical = 15.dp)
            ) {
              CircularProgressIndicator(
                modifier = Modifier
                  .width(48.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
              )
            }
          }

          is SettingsUiState.Success -> {
            SettingsPanelContent(settingsUiState, onBooleanSettingUpdated)
          }
        }
        SettingsSectionDivider()
        AboutItBookstoreText(openExternalUrl)
        SettingsSectionDivider()
        LinksPanelContent(openExternalUrl, openOssLicencesInfo)
        SettingsSectionDivider()
      }
    },
    confirmButton = {
      Text(
        text = stringResource(R.string.text_settings_dismiss),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .clickable {
            onDialogDismissed()
          },
      )
    },
  )
}

