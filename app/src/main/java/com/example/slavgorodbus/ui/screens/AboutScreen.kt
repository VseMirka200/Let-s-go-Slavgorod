package com.example.slavgorodbus.ui.screens // Убедитесь, что пакет указан верно для вашего проекта

import android.content.Intent // Для создания намерения открыть URL или отправить email
import android.net.Uri // Для парсинга URL
import androidx.compose.foundation.clickable // Для обработки нажатий на текстовые элементы (ссылки)
import androidx.compose.foundation.layout.* // Основные компоненты для компоновки UI
import androidx.compose.material.icons.Icons // Стандартные иконки Material
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Иконка "Назад" (адаптируется к RTL)
import androidx.compose.material3.* // Компоненты Material Design 3 (Scaffold, TopAppBar, Text и т.д.)
import androidx.compose.runtime.Composable // Основная аннотация для Composable-функций
import androidx.compose.ui.Alignment // Для выравнивания элементов внутри контейнеров
import androidx.compose.ui.Modifier // Модификаторы для настройки внешнего вида и поведения UI-элементов
import androidx.compose.ui.platform.LocalContext // Для получения текущего Context (необходим для Intent'ов)
import androidx.compose.ui.res.stringResource // Для доступа к строковым ресурсам (локализация)
import androidx.compose.ui.text.font.FontWeight // Для установки жирности шрифта
import androidx.compose.ui.text.style.TextDecoration // Для добавления подчеркивания к тексту (ссылки)
import androidx.compose.ui.unit.dp // Единицы измерения для отступов, размеров и т.д.
import androidx.compose.ui.unit.sp // <-- ДОБАВЛЕН ИМПОРТ ДЛЯ sp (масштабируемые пиксели для шрифта)
import com.example.slavgorodbus.R // Класс R для доступа к ресурсам проекта (строки, изображения и т.д.)

// Оптимизация для экспериментальных API Material 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit // Лямбда-функция, вызываемая при нажатии кнопки "Назад" для возврата на предыдущий экран
) {
    // Получение текущего Android Context. Он необходим для создания Intent'ов,
    // например, для открытия веб-ссылки или почтового клиента.
    val context = LocalContext.current

    // --- Информация о разработчике ---
    val developerName = "Олюшин Владислав Викторович"
    val developerVkUrl = "https://vk.com/vsemirka200"
    val developerGitHubUrl = "https://github.com/VseMirka200/Let-s-go-Slavgorod"
    val linkTextVk = "ВКонтакте"
    val linkTextGitHub = "GitHub"

    // Scaffold предоставляет стандартную структуру экрана Material Design
    Scaffold(
        topBar = {
            // Верхняя панель приложения (AppBar)
            TopAppBar(
                title = { Text("О программе") }, // Заголовок экрана
                navigationIcon = {
                    // Иконка для навигации (кнопка "Назад")
                    IconButton(onClick = onNavigateBack) { // При нажатии вызывается переданная лямбда onNavigateBack
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Иконка "стрелка назад"
                            contentDescription = "Назад" // Описание для доступности
                        )
                    }
                },
                // Настройка цветов для TopAppBar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer, // Цвет фона AppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer, // Цвет текста заголовка
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer // Цвет иконки навигации
                )
            )
        }
    ) { paddingValues -> // paddingValues содержит отступы, необходимые из-за системных элементов (например, TopAppBar)

        // Основной контейнер для контента экрана, располагается вертикально
        Column(
            modifier = Modifier
                .fillMaxSize() // Занимает все доступное пространство
                .padding(paddingValues) // Применяет отступы от Scaffold (чтобы контент не перекрывался TopAppBar)
                .padding(16.dp), // Дополнительные внутренние отступы для всего контента Column
            horizontalAlignment = Alignment.Start // Выравнивание всего содержимого Column по левому краю
        ) {
            // Название приложения
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Секция "Разработчик"
            Text(
                "Разработал:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = developerName,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Секция "Ссылки"
            Text(
                "Ссылки:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Первая ссылка (ВКонтакте)
            Text(
                text = linkTextVk,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 18.sp // <-- ИЗМЕНЕНО: Увеличен размер шрифта для ссылки
                ),
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(developerVkUrl))
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        android.widget.Toast.makeText(
                            context,
                            "Не удалось открыть ссылку. Проверьте, установлен ли браузер.",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Вторая ссылка (GitHub)
            Text(
                text = linkTextGitHub,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 18.sp // <-- ИЗМЕНЕНО: Увеличен размер шрифта для ссылки
                ),
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(developerGitHubUrl))
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        android.widget.Toast.makeText(
                            context,
                            "Не удалось открыть ссылку. Проверьте, установлен ли браузер.",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}