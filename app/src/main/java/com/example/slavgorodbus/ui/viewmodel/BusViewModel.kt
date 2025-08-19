package com.example.slavgorodbus.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.slavgorodbus.data.model.BusRoute // Модель данных для маршрута автобуса
import com.example.slavgorodbus.data.model.BusSchedule // Модель данных для расписания автобуса
import com.example.slavgorodbus.data.model.FavoriteTime // Модель данных для избранного времени отправления
import com.example.slavgorodbus.notification.BusNotificationService // Сервис для отправки уведомлений
import kotlinx.coroutines.flow.MutableStateFlow // Изменяемый StateFlow для управления состоянием UI
import kotlinx.coroutines.flow.StateFlow // Неизменяемый StateFlow для предоставления состояния UI наружу
import kotlinx.coroutines.flow.asStateFlow // Преобразование MutableStateFlow в StateFlow

// ViewModel для управления логикой, связанной с автобусными маршрутами и расписаниями
class BusViewModel : ViewModel() {

    // Приватный изменяемый StateFlow для внутреннего управления состоянием UI.
    // Содержит список маршрутов, статус загрузки и возможные ошибки.
    private val _uiState = MutableStateFlow(BusUiState())
    // Публичный неизменяемый StateFlow, на который подписывается UI для получения обновлений состояния.
    val uiState: StateFlow<BusUiState> = _uiState.asStateFlow()

    // Приватный изменяемый StateFlow для хранения текущего поискового запроса.
    private val _searchQuery = MutableStateFlow("")
    // Публичный неизменяемый StateFlow для поискового запроса.
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Приватный изменяемый StateFlow для хранения списка избранных времен отправления.
    private val _favoriteTimes = MutableStateFlow<List<FavoriteTime>>(emptyList())
    // Публичный неизменяемый StateFlow для списка избранных времен.
    val favoriteTimes: StateFlow<List<FavoriteTime>> = _favoriteTimes.asStateFlow()

    // Пример статичного списка маршрутов. В реальном приложении эти данные обычно загружаются
    // из сети, базы данных или локального файла.
    private val sampleRoutes = listOf(
        BusRoute(
            id = "102",
            routeNumber = "102",
            name = "Славгород - Яровое",
            description = "Маршрут между Славгород и Яровое",
            startStop = "Славгород (Рынок)",
            endStop = "Яровое (МСЧ-128)",
            // isFavorite = false // По умолчанию маршрут не в избранном
        )
        // Сюда можно добавить другие маршруты
    )

    // Приватный изменяемый StateFlow, хранящий текущий полный список маршрутов (до фильтрации).
    // Инициализируется примерами маршрутов.
    private val routes = MutableStateFlow(sampleRoutes)

    // Блок инициализации ViewModel. Вызывается при создании экземпляра ViewModel.
    init {
        loadRoutes() // Загрузка начального списка маршрутов при инициализации.
    }

    // Обрабатывает изменение поискового запроса из UI.
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query // Обновляем значение поискового запроса.
        if (query.isBlank()) {
            // Если запрос пуст, загружаем (отображаем) все маршруты.
            loadRoutes()
        } else {
            // Иначе, фильтруем маршруты по запросу.
            searchRoutes(query)
        }
    }

    // Переключает статус "избранное" для маршрута.
    // Принимает `Context` для возможности отправки уведомлений.
    fun toggleFavorite(route: BusRoute, context: Context? = null) {
        val wasFavorite = route.isFavorite // Запоминаем предыдущее состояние "избранное".
        // Создаем новый список маршрутов, обновляя статус isFavorite для выбранного маршрута.
        val updatedRoutes = routes.value.map {
            if (it.id == route.id) it.copy(isFavorite = !it.isFavorite) else it
        }
        routes.value = updatedRoutes // Обновляем основной список маршрутов.
        updateUiState() // Обновляем состояние UI, чтобы отразить изменения.

        // Если маршрут был добавлен в избранное и контекст доступен, показываем уведомление.
        if (!wasFavorite && context != null) {
            val notificationService = BusNotificationService(context)
            notificationService.showFavoriteRouteNotification(
                route.routeNumber,
                "Маршрут ${route.name} добавлен в избранное. Вы будете получать уведомления об отправке."
            )
        }
    }

    // Загружает (или обновляет) список маршрутов в _uiState.
    // В данном случае, просто копирует текущее значение из `routes`.
    private fun loadRoutes() {
        _uiState.value = _uiState.value.copy(
            routes = routes.value, // Устанавливаем текущий список маршрутов.
            isLoading = false,    // Статус загрузки - false (загрузка завершена).
            error = null          // Ошибок нет.
        )
    }

    // Фильтрует маршруты на основе поискового запроса и обновляет _uiState.
    private fun searchRoutes(query: String) {
        // Фильтруем маршруты: номер или название должны содержать запрос (без учета регистра).
        val filteredRoutes = routes.value.filter {
            it.routeNumber.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true)
        }
        _uiState.value = _uiState.value.copy(
            routes = filteredRoutes, // Устанавливаем отфильтрованный список.
            isLoading = false,
            error = null
        )
    }

    // Обновляет состояние UI (_uiState) в зависимости от текущего поискового запроса.
    // Вызывается после изменений в `routes` (например, после toggleFavorite).
    private fun updateUiState() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isBlank()) {
            // Если запрос пуст, показываем все маршруты.
            _uiState.value = _uiState.value.copy(routes = routes.value)
        } else {
            // Иначе, применяем текущий фильтр.
            searchRoutes(currentQuery)
        }
    }

    // Возвращает маршрут по его ID.
    fun getRouteById(routeId: String?): BusRoute? {
        // Ищет маршрут в текущем (полном) списке маршрутов.
        return routes.value.find { it.id == routeId }
    }

    // Добавляет время отправления в список избранных.
    // Принимает `Context` для возможности отправки уведомлений.
    fun addFavoriteTime(schedule: BusSchedule, context: Context? = null) {
        // Создаем объект FavoriteTime на основе данных расписания.
        val favoriteTime = FavoriteTime(
            id = schedule.id, // ID элемента расписания используется как ID для избранного времени
            routeId = schedule.routeId,
            departureTime = schedule.departureTime,
            arrivalTime = schedule.arrivalTime,
            stopName = schedule.stopName
        )

        // Получаем текущий список избранных времен и преобразуем его в изменяемый список.
        val currentFavorites = _favoriteTimes.value.toMutableList()
        // Добавляем новое избранное время, только если его еще нет в списке (проверка по ID).
        if (!currentFavorites.any { it.id == favoriteTime.id }) {
            currentFavorites.add(favoriteTime)
            _favoriteTimes.value = currentFavorites // Обновляем StateFlow с избранными временами.

            // Если контекст доступен, показываем уведомление о добавлении времени в избранное.
            if (context != null) {
                val notificationService = BusNotificationService(context)
                notificationService.showBusDepartureNotification( // Метод для уведомления об отправке
                    schedule.routeId, // Или можно использовать favoriteTime.routeId
                    favoriteTime.departureTime,
                    favoriteTime.stopName
                )
            }
        }
    }

    // Удаляет время отправления из списка избранных по ID элемента расписания.
    fun removeFavoriteTime(scheduleId: String) {
        val currentFavorites = _favoriteTimes.value.toMutableList()
        // Удаляем все элементы, у которых ID совпадает с переданным scheduleId.
        currentFavorites.removeAll { it.id == scheduleId }
        _favoriteTimes.value = currentFavorites // Обновляем StateFlow.
    }

    // Проверяет, добавлено ли конкретное время отправления (по ID) в избранное.
    fun isFavoriteTime(scheduleId: String): Boolean {
        return _favoriteTimes.value.any { it.id == scheduleId }
    }

    // Возвращает текущий список всех избранных времен.
    // Может использоваться, например, на экране "Избранное время".
    fun getFavoriteTimes(): List<FavoriteTime> {
        return _favoriteTimes.value
    }
}

// Data class для представления состояния UI, связанного с маршрутами.
// Содержит список маршрутов для отображения, флаг загрузки и возможное сообщение об ошибке.
data class BusUiState(
    val routes: List<BusRoute> = emptyList(), // Список маршрутов для отображения в UI
    val isLoading: Boolean = false,          // Флаг, указывающий, идет ли загрузка данных
    val error: String? = null                // Сообщение об ошибке, если что-то пошло не так
)