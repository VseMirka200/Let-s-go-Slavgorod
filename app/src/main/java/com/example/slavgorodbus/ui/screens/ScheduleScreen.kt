package com.example.slavgorodbus.ui.screens

// Импорты для работы с макетами, списками и компонентами Material 3
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.* // Для Composable, remember, State и т.д.
import androidx.compose.ui.Alignment // Для выравнивания элементов
import androidx.compose.ui.Modifier // Модификаторы для настройки UI элементов
import androidx.compose.ui.text.font.FontWeight // Для установки жирности шрифта
import androidx.compose.ui.unit.dp // Единицы измерения для отступов, размеров и т.д.
import androidx.compose.ui.unit.sp // Единицы измерения для размеров шрифта
import com.example.slavgorodbus.data.model.BusRoute // Модель данных для маршрута
import com.example.slavgorodbus.data.model.BusSchedule // Модель данных для элемента расписания
import com.example.slavgorodbus.ui.components.ScheduleCard // Composable для отображения карточки с временем расписания
import com.example.slavgorodbus.ui.viewmodel.BusViewModel // ViewModel для бизнес-логики

// Оптимизация для экспериментальных API Material 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    route: BusRoute?, // Принимает объект BusRoute (может быть null, если маршрут не выбран/не найден)
    onBackClick: () -> Unit, // Лямбда-функция для обработки нажатия кнопки "Назад"
    viewModel: BusViewModel? = null // Экземпляр ViewModel (опциональный, для работы с избранным временем)
) {
    // Получение списка расписаний.
    // `remember` используется для того, чтобы `generateSampleSchedules` вызывалась только один раз
    // при первой композиции или при изменении `route`.
    // В реальном приложении здесь бы происходил запрос к ViewModel для получения расписаний.
    val schedules = remember(route) { // Ключ `route` гарантирует пересчет, если маршрут изменился
        if (route != null) {
            // Если маршрут предоставлен, генерируем для него пример расписания.
            generateSampleSchedules(route.id)
        } else {
            // Если маршрут не предоставлен, возвращаем пустой список.
            emptyList()
        }
    }

    // Scaffold предоставляет стандартную структуру экрана Material Design
    Scaffold(
        topBar = {
            // Верхняя панель приложения (AppBar)
            TopAppBar(
                title = {
                    // Заголовок AppBar, отображает номер маршрута, если он есть.
                    Text(
                        text = "Расписание маршрута ${route?.routeNumber ?: ""}", // Используем элвис-оператор для случая, когда route равен null
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    // Иконка для навигации (обычно кнопка "Назад")
                    IconButton(onClick = onBackClick) { // При нажатии вызывается переданная лямбда onBackClick
                        // Здесь должна быть иконка, например, Icons.AutoMirrored.Filled.ArrowBack
                        // Если IconButton пуст, он будет отображаться как пустая кликабельная область.
                        // Пример: Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues -> // paddingValues содержит отступы от системных элементов (например, TopAppBar)

        // Условное отображение контента в зависимости от того, передан ли маршрут (`route`)
        if (route == null) {
            // Если маршрут не выбран (route == null), показываем сообщение с просьбой выбрать маршрут.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Применяем отступы
                contentAlignment = Alignment.Center // Центрируем содержимое
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Расстояние между текстами
                ) {
                    Text(
                        text = "Выберите маршрут",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Для просмотра расписания выберите маршрут из списка",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Если маршрут выбран, отображаем информацию о нем и список расписаний.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Применяем отступы
                contentPadding = PaddingValues(vertical = 8.dp) // Внутренние отступы для списка
            ) {
                // Первый элемент списка - карточка с информацией о маршруте.
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth() // Карточка занимает всю ширину
                            .padding(horizontal = 16.dp, vertical = 8.dp), // Внешние отступы для карточки
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer // Цвет фона карточки
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp) // Внутренние отступы в карточке
                        ) {
                            Text(
                                text = "Маршрут №${route.routeNumber}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp)) // Небольшой отступ

                            Text(
                                // В реальном приложении здесь должно быть название маршрута из route.name
                                // Сейчас здесь статичный текст, который может не соответствовать `route.routeNumber`
                                text = route.name, // Используем route.name для корректного отображения
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // Отображение списка элементов расписания (время отправления и прибытия)
                items(schedules) { schedule -> // Итерация по списку `schedules`
                    // Проверяем, добавлено ли это время в избранное через ViewModel
                    val isFavorite = viewModel?.isFavoriteTime(schedule.id) ?: false

                    // Для каждого элемента расписания отображаем ScheduleCard
                    ScheduleCard(
                        schedule = schedule, // Передаем данные элемента расписания
                        isFavorite = isFavorite, // Передаем статус "избранное"
                        onFavoriteClick = { // Лямбда-функция для обработки клика на иконку "избранное"
                            viewModel?.let { vm -> // Выполняем, только если viewModel не null
                                if (isFavorite) {
                                    vm.removeFavoriteTime(schedule.id) // Удаляем из избранного
                                } else {
                                    vm.addFavoriteTime(schedule) // Добавляем в избранное
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

// Приватная функция для генерации примерного списка расписаний.
// В реальном приложении эти данные должны приходит
private fun generateSampleSchedules(routeId: String): List<BusSchedule> {
    // Возвращает список расписаний в зависимости от routeId.
    // Пока реализовано только для "102".
    return when (routeId) {
        "102" -> listOf(
            BusSchedule("102_1", "102", "Славгород (Рынок)", "06:25", "07:00", 1),
            BusSchedule("102_2", "102", "Славгород (Рынок)", "06:45", "07:20", 1),
            BusSchedule("102_3", "102", "Славгород (Рынок)", "07:00", "07:35", 1),
            BusSchedule("102_4", "102", "Славгород (Рынок)", "07:20", "07:55", 1),
            BusSchedule("102_5", "102", "Славгород (Рынок)", "07:40", "08:20", 1),
            BusSchedule("102_6", "102", "Славгород (Рынок)", "08:00", "08:40", 1),
            BusSchedule("102_7", "102", "Славгород (Рынок)", "08:25", "09:00", 1),
            BusSchedule("102_8", "102", "Славгород (Рынок)", "08:40", "09:20", 1),
            BusSchedule("102_9", "102", "Славгород (Рынок)", "09:00", "09:40", 1),
            BusSchedule("102_10", "102", "Славгород (Рынок)", "09:20", "10:00", 1),
            BusSchedule("102_11", "102", "Славгород (Рынок)", "09:35", "10:15", 1),
            BusSchedule("102_12", "102", "Славгород (Рынок)", "10:00", "10:35", 1),
            BusSchedule("102_13", "102", "Славгород (Рынок)", "10:25", "11:10", 1),
            BusSchedule("102_14", "102", "Славгород (Рынок)", "10:50", "11:30", 1),
            BusSchedule("102_15", "102", "Славгород (Рынок)", "11:10", "11:55", 1),
            BusSchedule("102_16", "102", "Славгород (Рынок)", "11:35", "12:20", 1),
            BusSchedule("102_17", "102", "Славгород (Рынок)", "12:05", "12:40", 1),
            BusSchedule("102_18", "102", "Славгород (Рынок)", "12:30", "13:05", 1),
            BusSchedule("102_19", "102", "Славгород (Рынок)", "12:55", "13:30", 1),
            BusSchedule("102_20", "102", "Славгород (Рынок)", "13:15", "13:55", 1),
            BusSchedule("102_21", "102", "Славгород (Рынок)", "13:35", "14:15", 1),
            BusSchedule("102_22", "102", "Славгород (Рынок)", "14:05", "14:45", 1),
            BusSchedule("102_23", "102", "Славгород (Рынок)", "14:30", "15:10", 1),
            BusSchedule("102_24", "102", "Славгород (Рынок)", "14:55", "15:30", 1),
            BusSchedule("102_25", "102", "Славгород (Рынок)", "15:20", "15:55", 1),
            BusSchedule("102_26", "102", "Славгород (Рынок)", "15:45", "16:20", 1),
            BusSchedule("102_27", "102", "Славгород (Рынок)", "16:10", "16:45", 1),
            BusSchedule("102_28", "102", "Славгород (Рынок)", "16:35", "17:10", 1),
            BusSchedule("102_29", "102", "Славгород (Рынок)", "17:05", "17:40", 1),
            BusSchedule("102_30", "102", "Славгород (Рынок)", "17:25", "18:10", 1),
            BusSchedule("102_31", "102", "Славгород (Рынок)", "17:50", "18:35", 1),
            BusSchedule("102_32", "102", "Славгород (Рынок)", "18:20", "19:00", 1),
            BusSchedule("102_33", "102", "Славгород (Рынок)", "18:50", "19:25", 1),
            BusSchedule("102_34", "102", "Славгород (Рынок)", "19:20", "20:00", 1),
            BusSchedule("102_35", "102", "Славгород (Рынок)", "20:00", "20:30", 1),
            BusSchedule("102_36", "102", "Славгород (Рынок)", "20:30", "21:00", 1)
        )
        // Сюда можно добавить расписания
        else -> emptyList() // Если ID марш
    }
}
