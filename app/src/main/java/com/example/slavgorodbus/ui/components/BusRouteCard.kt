package com.example.slavgorodbus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slavgorodbus.data.model.BusRoute
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusRouteCard(
    route: BusRoute,                          // Объект с данными о маршруте
    onRouteClick: (BusRoute) -> Unit,         // Лямбда-функция, вызываемая при клике на всю карточку (для перехода к расписанию)
    onFavoriteClick: (BusRoute) -> Unit,      // Лямбда-функция, вызываемая при клике на иконку "избранное"
    modifier: Modifier = Modifier             // Модификатор для внешней настройки компонента
) {
    // Получение текущего Android Context. Может быть использован для различных операций,
    // например, для отображения Toast-уведомлений, хотя в данном компоненте он явно не используется
    // после получения.
    LocalContext.current

    // Card - компонент Material Design для отображения контента на карточке с тенью.
    Card(
        modifier = modifier // Применение внешних модификаторов
            .fillMaxWidth() // Растягивает карточку на всю доступную ширину
            .padding(horizontal = 16.dp, vertical = 8.dp) // Внешние отступы для карточки
            .clickable { onRouteClick(route) }, // Делает всю карточку кликабельной, вызывая onRouteClick при нажатии
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Устанавливает высоту тени карточки
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Цвет фона карточки (из текущей темы)
        )
    ) {
        // Column - располагает дочерние элементы вертикально, один под другим.
        Column(
            modifier = Modifier.padding(16.dp) // Внутренние отступы для содержимого карточки
        ) {
            // Первый Row: номер маршрута, название, описание и кнопка "избранное".
            Row(
                modifier = Modifier.fillMaxWidth(), // Растягивает Row на всю ширину Column
                horizontalArrangement = Arrangement.SpaceBetween, // Распределяет пространство между дочерними элементами
                // (группа слева и кнопка справа).
                verticalAlignment = Alignment.CenterVertically    // Выравнивает дочерние элементы по центру по вертикали.
            ) {
                // Левая часть: кружок с номером маршрута, название и описание.
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Выравнивание элементов внутри этого Row по вертикальному центру
                    horizontalArrangement = Arrangement.spacedBy(12.dp) // Расстояние в 12dp между элементами этого Row
                ) {
                    // Box для отображения цветного кружка с номером маршрута.
                    Box(
                        modifier = Modifier
                            .size(48.dp) // Фиксированный размер для кружка
                            .clip(RoundedCornerShape(12.dp)) // Скругляет углы, делая его похожим на кружок/квадрат с скругленными углами
                            // Устанавливает цвет фона кружка, парся его из строки route.color.
                            // Внимание: если route.color не является валидным представлением цвета, это может вызвать ошибку.
                            .background(Color(route.color.toColorInt())),
                        contentAlignment = Alignment.Center // Центрирует текст (номер маршрута) внутри Box
                    ) {
                        // Текст номера маршрута внутри кружка.
                        Text(
                            text = route.routeNumber,
                            color = Color.White, // Цвет текста номера маршрута (белый)
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Column для названия и описания маршрута.
                    Column {
                        Text(
                            text = route.name, // Название маршрута
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold, // Полужирное начертание
                            color = MaterialTheme.colorScheme.onSurface // Цвет текста на фоне поверхности
                        )
                        Text(
                            text = route.description, // Описание маршрута
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, // Приглушенный цвет текста
                            maxLines = 2 // Ограничивает описание двумя строками (если оно длиннее, будет обрезано с многоточием)
                        )
                    }
                }

                // IconButton для добавления/удаления из избранного.
                IconButton(
                    onClick = {
                        onFavoriteClick(route) // Вызывает лямбду при нажатии, передавая текущий маршрут
                    }
                ) {
                    Icon(
                        // Выбор иконки (заполненное сердце или контур) в зависимости от состояния route.isFavorite.
                        imageVector = if (route.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        // Описание контента для доступности.
                        contentDescription = if (route.isFavorite) "Remove from favorites" else "Add to favorites",
                        // Цвет иконки.
                        tint = if (route.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Spacer для добавления вертикального отступа между двумя основными рядами.
            Spacer(modifier = Modifier.height(12.dp))

            // Второй Row: информация о начальной и конечной остановках.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Распределяет пространство
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левая часть: иконка местоположения и текст "Начальная остановка -> Конечная остановка".
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp) // Небольшой отступ между иконкой и текстом
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn, // Иконка "местоположение"
                        contentDescription = null, // Можно добавить описание, например, "Маршрут следования"
                        tint = MaterialTheme.colorScheme.primary, // Цвет иконки
                        modifier = Modifier.size(16.dp) // Меньший размер иконки
                    )
                    Text(
                        text = "${route.startStop} → ${route.endStop}", // Отображение начальной и конечной остановок
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Правая часть второго ряда. В текущем коде она пустая.
                // Сюда можно было бы добавить, например, информацию о длительности маршрута или интервалах.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Пока что здесь ничего нет.
                }
            }
        }
    }
}
