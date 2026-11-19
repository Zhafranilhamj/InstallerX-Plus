// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.installer.components

import com.rosan.installer.domain.engine.model.install.InstallPhase
import com.rosan.installer.ui.page.main.installer.InstallerStage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InstallProgressTest {
    private val writing = InstallerStage.Installing(0.4f, 1, 1, null, InstallPhase.WRITING)

    @Test
    fun singlePackageSwitchesToIndeterminateAfterWriting() {
        assertEquals(0.4f, writing.measuredProgress())
        assertNull(writing.copy(progress = 1f, phase = InstallPhase.INSTALLING).measuredProgress())
    }

    @Test
    fun batchRetainsMeasuredProgressDuringInstall() {
        assertEquals(0.4f, writing.copy(total = 3, phase = InstallPhase.INSTALLING).measuredProgress())
    }

    @Test
    fun unknownProgressAndOutOfRangeValuesAreSafeForAccessibility() {
        listOf(0f, -1f, Float.NaN, Float.POSITIVE_INFINITY).forEach { assertNull(writing.copy(progress = it).measuredProgress()) }
        assertEquals(1f, writing.copy(progress = 1.01f).measuredProgress())
    }
}
