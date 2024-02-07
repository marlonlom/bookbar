package dev.marlonlom.apps.bookbar.ui.main.scaffold.tablets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.marlonlom.apps.bookbar.ui.navigation.BookbarRoute

/**
 * Expanded navigation drawer composable ui.
 *
 * @author marlonlom
 *
 * @param selectedPosition Selected index.
 * @param onSelectedPositionChanged Action for selected navigation bar item index changed.
 */
@Composable
fun ExpandedNavigationDrawer(
  selectedPosition: Int,
  onSelectedPositionChanged: (Int, String) -> Unit,
  content: @Composable () -> Unit,
) {
  PermanentNavigationDrawer(
    drawerContent = {
      PermanentDrawerSheet(
        modifier = Modifier
          .width(200.dp)
          .padding(20.dp),
      ) {
        NavigationDrawerContent(
          selectedPosition = selectedPosition,
          onSelectedPositionChanged = onSelectedPositionChanged,
        )
      }
    },
    content = content
  )
}

/**
 * Expanded navigation drawer content composable ui.
 *
 * @author marlonlom
 *
 * @param selectedPosition Selected index.
 * @param onSelectedPositionChanged Action for selected navigation bar item index changed.
 */
@Composable
internal fun NavigationDrawerContent(
  selectedPosition: Int, onSelectedPositionChanged: (Int, String) -> Unit
) {
  BookbarRoute.topDestinationRoutes.forEachIndexed { index, destination ->
    NavigationDrawerItem(
      shape = MaterialTheme.shapes.large,
      selected = selectedPosition == index,
      onClick = {
        onSelectedPositionChanged(index, destination.route)
      },
      icon = {
        Icon(
          imageVector = destination.icon!!, contentDescription = stringResource(id = destination.label)
        )
      },
      label = {
        Text(text = stringResource(id = destination.label))
      },
    )
  }
}

