package com.example.slavgorodbus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,                           // Текущий поисковый запрос (входное значение)
    onQueryChange: (String) -> Unit,         // Лямбда-функция, вызываемая при изменении текста в поле ввода
    onSearch: () -> Unit,                    // Лямбда-функция, вызываемая при нажатии кнопки "Поиск" на клавиатуре
    modifier: Modifier = Modifier,           // Модификатор для внешней настройки компонента
    placeholder: String = "Поиск маршрутов...", // Текст-подсказка, отображаемый, когда поле ввода пустое
    leadingIcon: ImageVector = Icons.Default.Search // Иконка, отображаемая в начале поля ввода (по умолчанию - лупа)
) {
    // Создание FocusRequester для возможности управления фокусом на этом поле ввода.
    // В данном коде он создается, но активно не используется для установки фокуса.
    // Его можно использовать, например, для автоматической установки фокуса при появлении экрана.
    val focusRequester = FocusRequester()

    // OutlinedTextField - это компонент Material Design для ввода текста с обводкой.
    OutlinedTextField(
        value = query, // Текущее значение текста в поле
        onValueChange = onQueryChange, // Функция, которая будет вызвана при каждом изменении текста
        modifier = modifier // Применение внешних модификаторов
            .fillMaxWidth() // Растягивает поле ввода на всю доступную ширину
            .padding(16.dp) // Добавляет отступы вокруг поля ввода (16dp со всех сторон)
            .focusRequester(focusRequester), // Привязывает FocusRequester к этому полю
        placeholder = { Text(placeholder) }, // Текст-подсказка
        leadingIcon = { // Иконка в начале поля ввода
            Icon(
                imageVector = leadingIcon, // Изображение иконки
                contentDescription = "Search", // Описание для доступности (рекомендуется использовать строковый ресурс)
                tint = MaterialTheme.colorScheme.onSurfaceVariant // Цвет иконки (обычно приглушенный цвет на фоне)
            )
        },
        trailingIcon = { // Иконка в конце поля ввода
            // Отображаем иконку "Очистить" (крестик) только если в поле ввода есть текст
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) { // Кнопка-иконка для очистки поля
                    Icon(
                        imageVector = Icons.Default.Clear, // Иконка "крестик"
                        contentDescription = "Clear search", // Описание для доступности (рекомендуется использовать строковый ресурс)
                        tint = MaterialTheme.colorScheme.onSurfaceVariant // Цвет иконки
                    )
                }
            }
        },
        singleLine = true, // Ограничивает ввод одной строкой
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Настраивает кнопку действия на клавиатуре
        // (здесь это кнопка "Поиск")
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),    // Определяет действие при нажатии кнопки "Поиск"
        // на клавиатуре (вызывает лямбду onSearch)
        // Настройка цветов для различных состояний поля ввода
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary, // Цвет рамки, когда поле в фокусе
            unfocusedBorderColor = MaterialTheme.colorScheme.outline, // Цвет рамки, когда поле не в фокусе
            focusedLabelColor = MaterialTheme.colorScheme.primary // Цвет метки (если используется), когда поле в фокусе
            // Другие цвета (текста, курсора и т.д.) будут использоваться по умолчанию из MaterialTheme
        ),
        // Форма поля ввода (использует среднюю закругленную форму из темы)
        shape = MaterialTheme.shapes.medium
    )
}