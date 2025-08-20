package com.example.slavgorodbus.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Для кнопки "Назад"
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight // <--- ДОБАВЛЕН ИМПОРТ
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // <--- ДОБАВЛЕН ИМПОРТ
import com.example.slavgorodbus.ui.viewmodel.AppTheme
import com.example.slavgorodbus.ui.viewmodel.ThemeViewModel
import com.example.slavgorodbus.ui.viewmodel.getThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel = getThemeViewModel(),
    onNavigateBack: (() -> Unit)? = null
) {
    val currentAppTheme by themeViewModel.currentTheme.collectAsState()
    var showThemeDropdown by remember { mutableStateOf(false) }
    val themeOptions = AppTheme.values()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Настройки", // Текст заголовка для этого экрана
                        // СТИЛЬ ЗАГОЛОВКА КАК В FavoriteTimesScreen:
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад"
                                // Цвет иконки будет унаследован от titleContentColor или можно задать явно navigationIconContentColor
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    // ЦВЕТА TOPAPPBAR КАК В FavoriteTimesScreen:
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    // Добавим цвет для иконки навигации для консистентности, если он нужен отдельно,
                    // иначе он унаследует от titleContentColor, если он не переопределен.
                    // В данном случае onPrimaryContainer подойдет.
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Тема приложения",
                // Стиль для заголовка секции можно оставить titleLarge или titleMedium,
                // так как это внутри контента, а не в TopAppBar.
                // Для соответствия внутреннему стилю FavoriteTimesScreen (если бы там были такие секции),
                // можно было бы использовать fontSize=18.sp, fontWeight=FontWeight.Medium
                // Но titleLarge/titleMedium из MaterialTheme.typography обычно предпочтительнее для секций.
                style = MaterialTheme.typography.titleLarge, // Оставим titleLarge для заголовка секции
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showThemeDropdown = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Внешний вид:",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = when (currentAppTheme) {
                                AppTheme.SYSTEM -> "Как в системе"
                                AppTheme.LIGHT -> "Светлая"
                                AppTheme.DARK -> "Темная"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary // Акцентный цвет для выбранной темы
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Выбрать тему",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                DropdownMenu(
                    expanded = showThemeDropdown,
                    onDismissRequest = { showThemeDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    themeOptions.forEach { theme ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    when (theme) {
                                        AppTheme.SYSTEM -> "Как в системе"
                                        AppTheme.LIGHT -> "Светлая"
                                        AppTheme.DARK -> "Темная"
                                    },
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
        }
    }
}