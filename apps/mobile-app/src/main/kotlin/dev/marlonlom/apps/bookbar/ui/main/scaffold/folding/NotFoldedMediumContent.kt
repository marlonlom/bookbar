package dev.marlonlom.apps.bookbar.ui.main.scaffold.folding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Not folded medium width content composable ui.
 *
 * @author marlonlom
 *
 * @param isLandscapeOrientation True/False is ui is in landscape orientation.
 * @param navigationRail Navigation rail composable ui.
 * @param leftContent Left content composable ui
 * @param rightContent Right content composable ui.
 */
@Composable
fun NotFoldedMediumContent(
  isLandscapeOrientation: Boolean,
  navigationRail: @Composable () -> Unit,
  leftContent: @Composable () -> Unit,
  rightContent: @Composable () -> Unit
) {

  val fraction = if (isLandscapeOrientation) 0.5f else 1f

  Row(modifier = Modifier.safeContentPadding()) {
    navigationRail()

    Column(
      modifier = Modifier
        .fillMaxWidth(fraction)
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.surface),
      verticalArrangement = Arrangement.Top
    ) {
      leftContent()
    }

    if (isLandscapeOrientation) {
      val detailBg: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(detailBg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
      ) {
        rightContent()
      }
    }

  }

}
