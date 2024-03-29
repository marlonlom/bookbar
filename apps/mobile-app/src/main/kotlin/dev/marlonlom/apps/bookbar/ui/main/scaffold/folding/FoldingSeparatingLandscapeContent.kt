package dev.marlonlom.apps.bookbar.ui.main.scaffold.folding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Folded content content composable ui when device is in separating posture
 * and landscape orientation.
 *
 * @author marlonlom
 *
 * @param navigationRail Navigation rail composable ui.
 * @param leftContent Left content composable ui
 * @param rightContent Right content composable ui.
 */
@Composable
fun FoldingSeparatingLandscapeContent(
  navigationRail: @Composable () -> Unit,
  leftContent: @Composable () -> Unit,
  rightContent: @Composable () -> Unit
) {
  Column {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.5f)
        .background(MaterialTheme.colorScheme.surface),
    ) {

      navigationRail()

      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
      ) {
        leftContent()
      }
    }

    val detailBg: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(detailBg),
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
      ) {
        rightContent()
      }
    }
  }
}
