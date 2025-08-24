package com.example.slavgorodbus.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.slavgorodbus.R
import com.example.slavgorodbus.ui.viewmodel.AppTheme
import com.example.slavgorodbus.ui.viewmodel.ThemeViewModel
import com.example.slavgorodbus.ui.viewmodel.getThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel = getThemeViewModel(),
    onNavigateToAbout: () -> Unit,
    modifier: Modifier
) {
    val currentAppTheme by themeViewModel.currentTheme.collectAsState()
    var showThemeDropdown by remember { mutableStateOf(false) }
    val themeOptions = remember { AppTheme.entries.toTypedArray() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_screen_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold) // <--- ИЗМЕНЕНО
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.settings_section_theme_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = modifier.padding(bottom = 8.dp)
            )
            Card(
                modifier = modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { showThemeDropdown = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.settings_appearance_label),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(
                                when (currentAppTheme) {
                                    AppTheme.SYSTEM -> R.string.theme_system
                                    AppTheme.LIGHT -> R.string.theme_light
                                    AppTheme.DARK -> R.string.theme_dark
                                }
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = stringResource(R.string.settings_select_theme_desc),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (showThemeDropdown) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = { showThemeDropdown = false },
                ) {
                    themeOptions.forEach { theme ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(
                                        when (theme) {
                                            AppTheme.SYSTEM -> R.string.theme_system
                                            AppTheme.LIGHT -> R.string.theme_light
                                            AppTheme.DARK -> R.string.theme_dark
                                        }
                                    ),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                themeViewModel.setTheme(theme)
                                showThemeDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(24.dp))

            Text(
                text = stringResource(R.string.settings_section_about_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = modifier.padding(bottom = 8.dp)
            )
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToAbout() },
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = modifier.size(24.dp)
                    )
                    Spacer(modifier = modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.about_screen_title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}