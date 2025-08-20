package com.example.slavgorodbus.ui.viewmodel // Убедитесь, что это ваш правильный пакет

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore // <--- ВАЖНЫЙ ИМПОРТ
import androidx.datastore.preferences.core.Preferences // <--- ВАЖНЫЙ ИМПОРТ
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore // <--- САМЫЙ ВАЖНЫЙ ИМПОРТ ДЛЯ ЭТОЙ ОШИБКИ
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// DataStore для сохранения настроек темы - НА ВЕРХНЕМ УРОВНЕ ФАЙЛА
val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

enum class AppTheme {
    SYSTEM, LIGHT, DARK
}

class ThemeViewModel(private val context: Context) : ViewModel() {

    private val themePreferencesKey = stringPreferencesKey("app_theme")

    // Используем context для доступа к свойству расширения
    val currentTheme = context.themeDataStore.data
        .map { preferences ->
            AppTheme.valueOf(preferences[themePreferencesKey] ?: AppTheme.SYSTEM.name)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            context.themeDataStore.edit { preferences ->
                preferences[themePreferencesKey] = theme.name
            }
        }
    }
}

@Composable
fun getThemeViewModel(): ThemeViewModel {
    val context = LocalContext.current.applicationContext
    return androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ThemeViewModelFactory(context)
    )
}

class ThemeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}