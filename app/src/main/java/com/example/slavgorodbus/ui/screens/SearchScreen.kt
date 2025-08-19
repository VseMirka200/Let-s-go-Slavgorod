package com.example.slavgorodbus.ui.screens

// Импорты для работы с макетами, списками, иконками и компонентами Material 3
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search // Иконка для состояний поиска
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // Для подписки на StateFlow из ViewModel
import androidx.compose.runtime.getValue // Для удобного доступа к значению StateFlow
import androidx.compose.ui.Alignment // Для выравнивания элементов
import androidx.compose.ui.Modifier // Модификаторы для настройки UI элементов
import androidx.compose.ui.platform.LocalContext // Для доступа к Context, если он нужен (например, для уведомлений)
import androidx.compose.ui.text.font.FontWeight // Для установки жирности шрифта
import androidx.compose.ui.unit.dp // Единицы измерения для отступов, размеров и т.д.
import androidx.compose.ui.unit.sp // Единицы измерения для размеров шрифта
import com.example.slavgorodbus.data.model.BusRoute // Модель данных для маршрута
import com.example.slavgorodbus.ui.components.BusRouteCard // Composable для отображения карточки маршрута
import com.example.slavgorodbus.ui.components.SearchBar // Composable для поля ввода поискового запроса
import com.example.slavgorodbus.ui.viewmodel.BusViewModel // ViewModel для бизнес-логики

// Оптимизация для экспериментальных API Material 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onRouteClick: (BusRoute) -> Unit, // Лямбда-функция, вызываемая при клике на маршрут (для навигации)
    viewModel: BusViewModel           // Экземпляр ViewModel для доступа к данным и логике
) {
    // Подписка на состояние UI из ViewModel. При изменении uiState, Composable будет перерисован.
    val uiState by viewModel.uiState.collectAsState()
    // Подписка на текущий поисковый запрос из ViewModel.
    val searchQuery by viewModel.searchQuery.collectAsState()
    // Получение текущего контекста (может понадобиться для некоторых операций, например, Toast или Notifications)
    val context = LocalContext.current

    // Scaffold предоставляет стандартную структуру экрана Material Design (TopAppBar, основной контент и т.д.)
    Scaffold(
        topBar = {
            // Верхняя панель приложения (AppBar)
            TopAppBar(
                title = {
                    // Заголовок AppBar
                    Text(
                        text = "Поиск маршрутов",
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

        // Основной контейнер для контента экрана, располагается вертикально
        Column(
            modifier = Modifier
                .fillMaxSize() // Занимает все доступное пространство
                .padding(paddingValues) // Применяет отступы от Scaffold
        ) {
            // Кастомный Composable для поля ввода поискового запроса
            SearchBar(
                query = searchQuery, // Текущий поисковый запрос
                onQueryChange = viewModel::onSearchQueryChange, // Функция обратного вызова при изменении текста в поле
                onSearch = { /* Поиск выполняется автоматически при изменении query, поэтому здесь может быть пусто */ },
                placeholder = "Поиск по номеру или названию маршрута..." // Текст-подсказка в поле ввода
            )

            // Условное отображение контента в зависимости от состояния поиска и UI
            if (searchQuery.isEmpty()) {
                // Если поисковый запрос пуст, показываем приветственное/информационное сообщение
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center // Центрируем содержимое Box
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, // Центрируем элементы внутри Column по горизонтали
                        verticalArrangement = Arrangement.spacedBy(16.dp) // Расстояние между элементами Column
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search, // Иконка поиска
                            contentDescription = null, // Описание для доступности (можно добавить)
                            modifier = Modifier.size(64.dp), // Размер иконки
                            tint = MaterialTheme.colorScheme.onSurfaceVariant // Цвет иконки
                        )
                        Text(
                            text = "Поиск автобусных маршрутов",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Введите номер или название маршрута для поиска",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (uiState.isLoading) {
                // Если данные загружаются, показываем индикатор прогресса
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator() // Стандартный индикатор загрузки
                }
            } else if (uiState.error != null) {
                // Если произошла ошибка при загрузке/поиске, показываем сообщение об ошибке
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search, // Можно использовать другую иконку для ошибки
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error // Цвет иконки соответствует цвету ошибки
                        )
                        Text(
                            text = "Ошибка поиска",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error // Цвет текста соответствует цвету ошибки
                        )
                        Text(
                            // Отображаем текст ошибки из uiState. Используем "!!" так как мы в блоке `error != null`.
                            // В более безопасной реализации стоит проверить еще раз или использовать `uiState.error.orEmpty()`.
                            text = uiState.error!!,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (uiState.routes.isEmpty()) {
                // Если поисковый запрос не пуст, но маршруты не найдены, показываем соответствующее сообщение
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Маршруты не найдены",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Попробуйте поиск с другими ключевыми словами",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Если есть результаты поиска (и нет загрузки/ошибок), отображаем список найденных маршрутов
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), // Занимает все доступное пространство
                    contentPadding = PaddingValues(vertical = 8.dp) // Отступы для содержимого списка
                ) {
                    // items - это функция LazyColumn для отображения списка элементов
                    // Она эффективно отображает только видимые на экране элементы.
                    items(uiState.routes) { route -> // Итерация по списку маршрутов из uiState
                        // Для каждого маршрута отображаем BusRouteCard
                        BusRouteCard(
                            route = route, // Передаем данные маршрута в карточку
                            onRouteClick = onRouteClick, // Передаем лямбду для обработки клика на карточку
                            // Лямбда для обработки клика на иконку "избранное" в карточке
                            onFavoriteClick = { clickedRoute -> viewModel.toggleFavorite(clickedRoute, context) }
                        )
                    }
                }
            }
        }
    }
}
