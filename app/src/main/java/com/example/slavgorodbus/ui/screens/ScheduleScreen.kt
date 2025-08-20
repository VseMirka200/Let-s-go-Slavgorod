package com.example.slavgorodbus.ui.screens

// Импорты для работы с макетами, списками и компонентами Material 3
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons // <--- ДОБАВЛЕНО
import androidx.compose.material.icons.automirrored.filled.ArrowBack // <--- ДОБАВЛЕНО
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
    val schedules = remember(route) {
        if (route != null) {
            generateSampleSchedules(route.id)
        } else {
            emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расписание маршрута №${route?.routeNumber ?: ""}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // <--- ИЗМЕНЕНО
                            contentDescription = "Назад"                       // <--- ИЗМЕНЕНО
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer // <--- ДОБАВЛЕНО для цвета иконки навигации
                )
            )
        }
    ) { paddingValues ->
        if (route == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Маршрут №${route.routeNumber}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = route.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                items(schedules) { schedule ->
                    val isFavorite = viewModel?.isFavoriteTime(schedule.id) ?: false
                    ScheduleCard(
                        schedule = schedule,
                        isFavorite = isFavorite,
                        onFavoriteClick = {
                            viewModel?.let { vm ->
                                if (isFavorite) {
                                    vm.removeFavoriteTime(schedule.id)
                                } else {
                                    vm.addFavoriteTime(schedule)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

// ... остальная часть файла (generateSampleSchedules) ...
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
            BusSchedule("102_10", "102", "Сलावгород (Рынок)", "09:20", "10:00", 1),
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