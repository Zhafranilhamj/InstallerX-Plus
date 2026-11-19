// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AeroAquariumGeometryTest {
    private val portrait = Size(714f, 1280f)
    private val landscape = Size(1280f, 714f)
    private val viewports = listOf(
        Size(360f, 800f),
        Size(1080f, 2400f),
        Size(2400f, 1080f),
        Size(800f, 1280f),
        Size(1280f, 800f),
        Size(900f, 900f),
        Size(320f, 1000f),
        Size(1600f, 400f),
    )

    @Test
    fun waterTracksTheArtworkRatherThanTheScreenOrientation() {
        assertEquals(0.964f, aquariumWaterline(portrait))
        assertEquals(0.955f, aquariumWaterline(landscape))
        for (artwork in listOf(portrait, landscape)) {
            for (viewport in viewports) {
                val scale = maxOf(viewport.width / artwork.width, viewport.height / artwork.height)
                val imageTop = viewport.height - artwork.height * scale
                val expectedTop = imageTop + artwork.height * aquariumWaterline(artwork) * scale
                val water = aquariumWaterBounds(viewport, artwork)
                assertEquals(expectedTop.coerceAtLeast(0f), water.top, 0.001f)
                assertEquals(viewport.height, water.bottom)
                assertEquals(viewport.width, water.width)
                assertTrue(water.height > 0f, "Water must stay visible with bottom-aligned cropping")
            }
        }
    }

    @Test
    fun theWholeFishStaysUnderwaterThroughoutTheAnimationAtEveryDensity() {
        for (artwork in listOf(portrait, landscape)) {
            for (viewport in viewports) {
                val water = aquariumWaterBounds(viewport, artwork)
                for (density in listOf(1f, 2f, 3f, 5f)) {
                    for (lane in listOf(0.25f, 0.78f)) {
                        repeat(1001) { frame ->
                            val pose = aquariumFishPose(water, frame / 1000f, lane, 19f * density)
                            // The tail's maximum extent is 0.65r; allow extra for the body outline.
                            assertTrue(pose.y - pose.radius * 0.7f >= water.top)
                            assertTrue(pose.y + pose.radius * 0.7f <= water.bottom)
                            assertTrue(pose.radius > 0f)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun animationWrapHappensOffscreenAndAlsoWorksWhenPaused() {
        val water = aquariumWaterBounds(Size(360f, 800f), portrait)
        val first = aquariumFishPose(water, 0f, 0.25f, 16f)
        val wrapped = aquariumFishPose(water, 1f, 0.25f, 16f)
        val last = aquariumFishPose(water, 0.999f, 0.25f, 16f)
        assertEquals(first, wrapped)
        assertTrue(first.x + first.radius < water.left)
        assertTrue(last.x - last.radius * 1.5f > water.right)
    }

    @Test
    fun unmeasuredOrInvalidImagesDoNotCreateInvalidClippingBounds() {
        assertEquals(Rect.Zero, aquariumWaterBounds(Size.Zero, portrait))
        assertEquals(Rect.Zero, aquariumWaterBounds(Size(360f, 800f), Size.Zero))
        assertEquals(Rect.Zero, aquariumWaterBounds(Size(Float.POSITIVE_INFINITY, 800f), portrait))
    }
}
