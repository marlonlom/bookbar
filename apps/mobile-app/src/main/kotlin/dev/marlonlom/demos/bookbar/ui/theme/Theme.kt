/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.demos.bookbar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Brand color schemes single object.
 *
 * @author marlonlom
 */
internal object BrandColorSchemes {

  val dark = darkColorScheme(
    primary = BrandColors.oceanInABowl,
    onPrimary = BrandColors.coachGreen,
    primaryContainer = BrandColors.raspberryLeafGreen,
    onPrimaryContainer = BrandColors.lightTurquoise,
    secondary = BrandColors.fluoriteBlue,
    onSecondary = BrandColors.mediumJungleGreen,
    secondaryContainer = BrandColors.garnetBlackGreen,
    onSecondaryContainer = BrandColors.turquoiseWhite,
    tertiary = BrandColors.polarSeas,
    onTertiary = BrandColors.crowberryBlue,
    tertiaryContainer = BrandColors.arapawa,
    onTertiaryContainer = BrandColors.icyLandscape,
    error = BrandColors.peachBud,
    errorContainer = BrandColors.chokecherry,
    onError = BrandColors.arcaviaRed,
    onErrorContainer = BrandColors.goGoPink,
    background = BrandColors.cocosBlack,
    onBackground = BrandColors.intrepidGrey,
    surface = BrandColors.cocosBlack,
    onSurface = BrandColors.intrepidGrey,
    surfaceVariant = BrandColors.sportingGreen,
    onSurfaceVariant = BrandColors.smoke,
    outline = BrandColors.sandstoneGreyGreen,
    inverseOnSurface = BrandColors.cocosBlack,
    inverseSurface = BrandColors.intrepidGrey,
    inversePrimary = BrandColors.posterGreen,
    surfaceTint = BrandColors.oceanInABowl,
    outlineVariant = BrandColors.sportingGreen,
    scrim = BrandColors.black,
  )

  val light = lightColorScheme(
    primary = BrandColors.posterGreen,
    onPrimary = BrandColors.white,
    primaryContainer = BrandColors.lightTurquoise,
    onPrimaryContainer = BrandColors.deepSlateGreen,
    secondary = BrandColors.chalcedonyGreen,
    onSecondary = BrandColors.white,
    secondaryContainer = BrandColors.turquoiseWhite,
    onSecondaryContainer = BrandColors.deepSlateGreen2,
    tertiary = BrandColors.mallardBlue,
    onTertiary = BrandColors.white,
    tertiaryContainer = BrandColors.icyLandscape,
    onTertiaryContainer = BrandColors.midnightDreams,
    error = BrandColors.redInferno,
    errorContainer = BrandColors.goGoPink,
    onError = BrandColors.white,
    onErrorContainer = BrandColors.dirtyLeather,
    background = BrandColors.whitePorcelain,
    onBackground = BrandColors.cocosBlack,
    surface = BrandColors.whitePorcelain,
    onSurface = BrandColors.cocosBlack,
    surfaceVariant = BrandColors.cautiousJade,
    onSurfaceVariant = BrandColors.sportingGreen,
    outline = BrandColors.arcticLichenGreen,
    inverseOnSurface = BrandColors.delicateWhite,
    inverseSurface = BrandColors.oil,
    inversePrimary = BrandColors.oceanInABowl,
    surfaceTint = BrandColors.posterGreen,
    outlineVariant = BrandColors.smoke,
    scrim = BrandColors.black,
  )

}

/**
 * Brand theme composable function.
 *
 * @author marlonlom
 *
 * @param darkTheme
 * @param dynamicColor
 * @param content
 */
@Composable
fun BookbarTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> BrandColorSchemes.dark
    else -> BrandColorSchemes.light
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.background.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = BookbarFont.appTypography,
    content = content
  )
}
