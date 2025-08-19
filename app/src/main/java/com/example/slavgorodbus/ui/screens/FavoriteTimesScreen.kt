package com.example.slavgorodbus.ui.screens

// Импорты для работы с макетами, списками, иконками и компонентами Material 3
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Для отображения списков в LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite // Иконка "Сердце" для избранного
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // Для подписки на StateFlow из ViewModel
import androidx.compose.runtime.getValue // Для удобного доступа к значению StateFlow
import androidx.compose.ui.Alignment // Для выравнивания элементов
import androidx.compose.ui.Modifier // Модификаторы для настройки UI элементов
import androidx.compose.ui.text.font.FontWeight // Для установки жирности шрифта
import androidx.compose.ui.unit.dp // Единицы измерения для отступов, размеров и т.д.
import androidx.compose.ui.unit.sp // Единицы измерения для размеров шрифта
import com.example.slavgorodbus.ui.components.ScheduleCard // Composable для отображения карточки с временем расписания
import com.example.slavgorodbus.ui.viewmodel.BusViewModel // ViewModel для доступа к данным и логике

// Оптимизация для экспериментальных API Material 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTimesScreen(
    viewModel: BusViewModel // Экземпляр ViewModel для доступа к списку избранных времен и управления ими
) {
    // Подписка на список избранных времен из ViewModel.
    // При изменении favoriteTimes в ViewModel, этот Composable будет автоматически перерисован.
    val favoriteTimes by viewModel.favoriteTimes.collectAsState()

    // Scaffold предоставляет стандартную структуру экрана Material Design (TopAppBar, основной контент и т.д.)
    Scaffold(
        topBar = {
            // Верхняя панель приложения (AppBar)
            TopAppBar(
                title = {
                    // Заголовок AppBar
                    Text(
                        text = "Избранное время",
                        fontSize = 24.sp, // Размер шрифта заголовка
                        fontWeight = FontWeight.Bold // Жирное начертание
                    )
                },
                // Настройка цветов для TopAppBar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer, // Цвет фона AppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer // Цвет текста заголовка
                )
            )
        }
    ) { paddingValues -> // paddingValues содержит отступы, необходимые из-за системных элементов (например, TopAppBar)

        // Условное отображение контента:
        // если список избранных времен пуст, показываем одно сообщение, иначе - список.
        if (favoriteTimes.isEmpty()) {
            // Если нет избранных времен, отображаем сообщение об этом.
            Box(
                modifier = Modifier
                    .fillMaxSize() // Занимает все доступное пространство
                    .padding(paddingValues), // Применяет отступы от Scaffold
                contentAlignment = Alignment.Center // Центрируем содержимое Box
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Центрируем элементы внутри Column по горизонтали
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Расстояние между элементами Column
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite, // Иконка "Сердце"
                        contentDescription = null, // Можно добавить описание для доступности, например, "Нет избранного"
                        modifier = Modifier.size(64.dp), // Размер иконки
                        tint = MaterialTheme.colorScheme.onSurfaceVariant // Нейтральный цвет иконки
                    )
                    Text(
                        text = "Нет избранного времени",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Добавьте время отправления в избранное, чтобы получать уведомления",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Если есть избранные времена, отображаем их в виде списка.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize() // Занимает все доступное пространство
                    .padding(paddingValues), // Применяет отступы от Scaffold
                contentPadding = PaddingValues(vertical = 8.dp) // Внутренние отступы для содержимого списка
            ) {
                // items - это функция LazyColumn для отображения списка элементов.
                // Она эффективно отображает только видимые на экране элементы, оптимизируя производительность.
                items(favoriteTimes) { favoriteTime -> // Итерация по списку favoriteTimes
                    // Преобразование объекта FavoriteTime в объект BusSchedule для использования с ScheduleCard.
                    // ВАЖНО: `dayOfWeek = 1` здесь установлено как заглушка.
                    // Если `dayOfWeek` важен для ScheduleCard или логики уведомлений,
                    // его нужно либо хранить в FavoriteTime, либо получать из другого источника.
                    val schedule = com.example.slavgorodbus.data.model.BusSchedule(
                        id = favoriteTime.id, // ID элемента расписания
                        routeId = favoriteTime.routeId, // ID маршрута
                        stopName = favoriteTime.stopName, // Название остановки
                        departureTime = favoriteTime.departureTime, // Время отправления
                        arrivalTime = favoriteTime.arrivalTime, // Время прибытия
                        dayOfWeek = 1 // День недели (здесь заглушка, см. комментарий выше)
                    )

                    // Для каждого избранного времени отображаем ScheduleCard.
                    ScheduleCard(
                        schedule = schedule, // Передаем преобразованные данные расписания
                        isFavorite = true, // Все элементы на этом экране по определению избранные
                        onFavoriteClick = { // Лямбда-функция для обработки клика на иконку "избранное"
                            // При клике на иконку "избранное" на этом экране, мы удаляем элемент из избранного.
                            viewModel.removeFavoriteTime(favoriteTime.id)
                        }
                    )
                }
            }
        }
    }
}