package dev.marlonlom.apps.bookbar.ui.main.scaffold.tablets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.marlonlom.apps.bookbar.ui.main.contents.BookbarAppState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author marlonlom
 *
 * @param appState Application ui state.
 * @param selectedPosition
 * @param onSelectedPositionChanged
 * @param firstContent
 * @param secondContent
 */
@ExperimentalCoroutinesApi
@Composable
fun TabletScaffoldContent(
  appState: BookbarAppState,
  selectedPosition: Int,
  onSelectedPositionChanged: (Int, String) -> Unit,
  firstContent: @Composable () -> Unit,
  secondContent: @Composable (Color) -> Unit
) {
  ExpandedNavigationDrawer(
    selectedPosition = selectedPosition,
    onSelectedPositionChanged = onSelectedPositionChanged,
  ) {
    val maxWith = if (appState.isLandscapeOrientation) 0.4f else 1f

    Row {
      Column(
        modifier = Modifier
          .safeContentPadding()
          .fillMaxWidth(maxWith)
          .fillMaxHeight()
          .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
      ) {
        firstContent()
      }

      if (appState.isLandscapeOrientation) {
        val detailPaneBackgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
        Column(
          modifier = Modifier
            .safeContentPadding()
            .fillMaxHeight()
            .background(detailPaneBackgroundColor)
            .padding(top = 20.dp)
        ) {
          secondContent(detailPaneBackgroundColor)
        }
      }
    }


  }
}
