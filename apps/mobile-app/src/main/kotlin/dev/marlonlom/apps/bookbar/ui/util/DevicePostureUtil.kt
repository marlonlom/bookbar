/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.marlonlom.apps.bookbar.ui.util

import android.graphics.Rect
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowLayoutInfo
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Information about the posture of the device.
 *
 * @author marlonlom
 */
sealed interface DevicePosture {

  /**
   * Normal posture posture of the device.
   *
   * @author marlonlom
   */
  data object NormalPosture : DevicePosture

  /**
   * Book posture posture of the foldable device.
   *
   * @author marlonlom
   *
   * @property hingePosition Hinge position.
   * @property orientation Orientation (Horizontal, Vertical)
   */
  data class BookPosture(
    val hingePosition: Rect,
    val orientation: FoldingFeature.Orientation
  ) : DevicePosture

  /**
   * Separating posture posture of the foldable device.
   *
   * @author marlonlom
   *
   * @property hingePosition Hinge position.
   * @property orientation Orientation (Horizontal, Vertical)
   */
  data class Separating(
    val hingePosition: Rect,
    var orientation: FoldingFeature.Orientation,
  ) : DevicePosture

  companion object {

    /**
     * Returns the device posture for selected layout information.
     *
     * @param layoutInfo Window layout information.
     */
    @JvmStatic
    fun fromLayoutInfo(layoutInfo: WindowLayoutInfo): DevicePosture {
      val foldingFeature =
        layoutInfo.displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

      return when {

        isBookPosture(foldingFeature) -> BookPosture(
          hingePosition = foldingFeature.bounds,
          orientation = foldingFeature.orientation
        )

        isSeparating(foldingFeature) -> Separating(
          hingePosition = foldingFeature.bounds,
          orientation = foldingFeature.orientation
        )

        else -> NormalPosture
      }
    }
  }

}

@OptIn(ExperimentalContracts::class)
fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
  contract { returns(true) implies (foldFeature != null) }
  return foldFeature?.state == FoldingFeature.State.HALF_OPENED
}

@OptIn(ExperimentalContracts::class)
fun isSeparating(foldFeature: FoldingFeature?): Boolean {
  contract { returns(true) implies (foldFeature != null) }
  return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
}
