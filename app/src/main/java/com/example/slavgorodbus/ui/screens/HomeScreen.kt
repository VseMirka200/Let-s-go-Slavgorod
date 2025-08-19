package com.example.slavgorodbus.ui.screens

// Импорты для работы с макетами, списками, иконками и компонентами Material 3
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus // Иконка для отображения при ошибках/пустом списке
import androidx.compose.material.icons.filled.Info // Иконка для кнопки "О программе"
import androidx.compose.material.icons.filled.Search // Иконка для кнопки "Поиск"
import androidx.compose.material3.CircularProgressIndicator // Индикатор загрузки
import androidx.compose.material3.ExperimentalMaterial3Api // Для использования экспериментальных API Material 3
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Компонент для кнопок с иконками
import androidx.compose.material3.MaterialTheme // Доступ к текущей теме (цвета, типографика)
import androidx.compose.material3.Scaffold // Основной макет экрана Material Design
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar // Верхняя панель приложения
import androidx.compose.material3.TopAppBarDefaults // Стандартные значения для TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // Для подписки на StateFlow из ViewModel
import androidx.compose.runtime.getValue // Для удобного доступа к значению StateFlow
import androidx.compose.ui.Alignment // Для выравнивания элементов
import androidx.compose.ui.Modifier // Модификаторы для настройки UI элементов
import androidx.compose.ui.platform.LocalContext // Для доступа к Context (например, для уведомлений)
import androidx.compose.ui.res.stringResource // Для доступа к строковым ресурсам (локализация)
import androidx.compose.ui.text.font.FontWeight // Для установки жирности шрифта
import androidx.compose.ui.unit.dp // Единицы измерения для отступов, размеров и т.д.
import androidx.compose.ui.unit.sp // Единицы измерения для размеров шрифта
import androidx.navigation.NavController // Для управления навигацией между экранами
import com.example.slavgorodbus.R // Класс R для доступа к ресурсам проекта (строки, изображения и т.д.)
import com.example.slavgorodbus.data.model.BusRoute // Модель данных для маршрута
import com.example.slavgorodbus.ui.components.BusRouteCard // Composable для отображения карточки маршрута
import com.example.slavgorodbus.ui.components.SearchBar // Composable для поля ввода поискового запроса
import com.example.slavgorodbus.ui.navigation.Screen // Класс для определения маршрутов навигации
import com.example.slavgorodbus.ui.viewmodel.BusViewModel // ViewModel для бизнес-логики

// Оптимизация для экспериментальных API Material 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRouteClick: (BusRoute) -> Unit, // Лямбда-функция, вызываемая при клике на маршрут (для навигации на экран расписания)
    viewModel: BusViewModel,           // Экземпляр ViewModel для доступа к данным и логике
    navController: NavController       // NavController для осуществления навигации на другие экраны (Поиск, О программе)
) {
    // Подписка на состояние UI из ViewModel. При изменении uiState, Composable будет перерисован.
    val uiState by viewModel.uiState.collectAsState()
    // Подписка на текущий поисковый запрос из ViewModel.
    val searchQuery by viewModel.searchQuery.collectAsState()
    // Получение текущего контекста (может понадобиться для некоторых операций, например, для показа Toast или отправки уведомлений)
    val context = LocalContext.current

    // Scaffold предоставляет стандартную структуру экрана Material Design (TopAppBar, основной контент и т.д.)
    Scaffold(
        topBar = {
            // Верхняя панель приложения (AppBar)
            TopAppBar(
                title = {
                    // Заголовок AppBar
                    Text(
                        // Используем строку из ресурсов для возможности локализации
                        // и централизованного управления текстами.
                        text = stringResource(id = R.string.app_name_actual), // Убедитесь, что R.string.app_name_actual определен в strings.xml
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = { // Секция для иконок действий в TopAppBar (справа)
                    // Кнопка для перехода на экран Поиска
                    IconButton(onClick = {
                        // Переходим на экран поиска, только если мы еще не на нем,
                        // чтобы избежать многократного добавления одного и того же экрана в стек навигации.
                        if (navController.currentDestination?.route != Screen.Search.route) {
                            navController.navigate(Screen.Search.route)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search, // Иконка поиска
                            contentDescription = stringResource(id = R.string.search_icon_description) // Описание для доступности
                        )
                    }

                    // Кнопка для перехода на экран "О программе"
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route) // Навигация на экран "О программе"
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Info, // Иконка "информация"
                            contentDescription = stringResource(id = R.string.about_icon_description) // Описание для доступности
                        )
                    }
                },
                // Настройка цветов для TopAppBar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer, // Цвет фона AppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer, // Цвет текста заголовка
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer // Цвет иконок действий
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
                onSearch = { /* Поиск выполняется автоматически при изменении query в ViewModel,
                                 поэтому здесь может быть пусто или дополнительная логика при нажатии "Enter" */ }
            )

            // Условное отображение контента в зависимости от состояния UI (загрузка, ошибка, пустой список, список с данными)
            if (uiState.isLoading) {
                // Если данные загружаются, показываем индикатор прогресса
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center // Центрируем индикатор
                ) {
                    CircularProgressIndicator() // Стандартный индикатор загрузки
                }
            } else if (uiState.error != null) {
                // Если произошла ошибка при загрузке, показываем сообщение об ошибке
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, // Центрируем элементы внутри Column по горизонтали
                        verticalArrangement = Arrangement.spacedBy(16.dp) // Расстояние между элементами Column
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBus, // Иконка автобуса
                            contentDescription = null, // Можно добавить описание для доступности, например, "Ошибка загрузки"
                            modifier = Modifier.size(64.dp), // Размер иконки
                            tint = MaterialTheme.colorScheme.error // Цвет иконки соответствует цвету ошибки
                        )
                        Text(
                            text = stringResource(id = R.string.error_loading_routes), // Текст ошибки из ресурсов
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error // Цвет текста соответствует цвету ошибки
                        )
                        Text(
                            // Отображаем текст ошибки из uiState.
                            // Внимание: использование "!!" (not-null assertion operator) может привести к NullPointerException,
                            // если error неожиданно окажется null. Безопаснее использовать `uiState.error.orEmpty()`
                            // или дополнительную проверку.
                            text = uiState.error!!,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (uiState.routes.isEmpty()) {
                // Если список маршрутов пуст (после загрузки и без ошибок)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBus,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant // Нейтральный цвет иконки
                        )
                        Text(
                            // Текст зависит от того, был ли введен поисковый запрос
                            text = if (searchQuery.isNotEmpty()) stringResource(id = R.string.routes_not_found)
                            else stringResource(id = R.string.routes_unavailable),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // Если был поисковый запрос, но ничего не найдено, предлагаем изменить запрос
                        if (searchQuery.isNotEmpty()) {
                            Text(
                                text = stringResource(id = R.string.try_changing_search_query),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                // Если есть маршруты для отображения (и нет загрузки/ошибок)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), // Занимает все доступное пространство
                    contentPadding = PaddingValues(vertical = 8.dp) // Отступы для содержимого списка
                ) {
                    // items - это функция LazyColumn для отображения списка элементов.
                    // Она эффективно отображает только видимые на экране элементы, оптимизируя производительность.
                    items(uiState.routes) { route -> // Итерация по списку маршрутов из uiState
                        // Для каждого маршрута отображаем BusRouteCard
                        BusRouteCard(
                            route = route, // Передаем данные маршрута в карточку
                            onRouteClick = onRouteClick, // Передаем лямбду для обработки клика на карточку (переход к расписанию)
                            // Лямбда для обработки клика на иконку "избранное" в карточке
                            onFavoriteClick = { currentRoute -> viewModel.toggleFavorite(currentRoute, context) }
                        )
                    }
                }
            }
        }
    }
}