// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosan.installer.ui.navigation.LocalNavigator
import com.rosan.installer.ui.theme.aero.InstallerScaffold
import com.rosan.installer.ui.theme.aero.LocalFrutigerAero
import com.rosan.installer.ui.theme.aero.aeroToolbar

/** Common Material 3 frame for the tools pages, used by both UI families. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScaffold(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val navigator = LocalNavigator.current
    InstallerScaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.aeroToolbar(),
                colors = if (LocalFrutigerAero.current) {
                    TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, scrolledContainerColor = Color.Transparent)
                } else {
                    TopAppBarDefaults.topAppBarColors()
                },
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}
