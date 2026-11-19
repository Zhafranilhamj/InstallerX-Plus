// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.sin

/** The lowest edge of the painted wave, with a little clearance, in each existing backdrop. */
internal fun aquariumWaterline(artwork: Size): Float = if (artwork.width > artwork.height) 0.955f else 0.964f

/** Must match ContentScale.Crop + Alignment.BottomCenter used by the wallpaper Image. */
internal fun aquariumWaterBounds(viewport: Size, artwork: Size): Rect {
    if (!viewport.width.isFinite() || !viewport.height.isFinite() || !artwork.width.isFinite() || !artwork.height.isFinite() ||
        viewport.width <= 0f || viewport.height <= 0f || artwork.width <= 0f || artwork.height <= 0f
    ) {
        return Rect.Zero
    }
    val cropScale = maxOf(viewport.width / artwork.width, viewport.height / artwork.height)
    val waterDepth = artwork.height * (1f - aquariumWaterline(artwork)) * cropScale
    return Rect(0f, (viewport.height - waterDepth).coerceAtLeast(0f), viewport.width, viewport.height)
}

internal data class AquariumFishPose(val x: Float, val y: Float, val radius: Float)

/** Account for the whole tail and bobbing, not just the center of the fish. */
internal fun aquariumFishPose(water: Rect, phase: Float, lane: Float, preferredRadius: Float): AquariumFishPose {
    val radius = preferredRadius.coerceIn(0f, water.height.coerceAtLeast(0f) * 0.26f)
    val clearance = radius * 0.8f
    val progress = (phase % 1f + 1f) % 1f
    val depth = (lane + 0.08f * sin(progress * 12.566371f)).coerceIn(0f, 1f)
    return AquariumFishPose(
        x = water.left - radius * 2f + (water.width + radius * 4f) * progress,
        y = water.top + clearance + (water.height - clearance * 2f) * depth,
        radius = radius,
    )
}
