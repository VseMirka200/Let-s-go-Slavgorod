package com.example.slavgorodbus.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slavgorodbus.data.model.BusSchedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCard(
    schedule: BusSchedule,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    // Card - это компонент Material Design для отображения контента на карточке с тенью.
    Card(
        modifier = modifier // Применение внешних модификаторов
            .fillMaxWidth() // Растягивает карточку на всю доступную ширину
            .padding(horizontal = 16.dp, vertical = 4.dp), // Внешние отступы для карточки (16dp по бокам, 4dp сверху/снизу)
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Устанавливает высоту тени карточки
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Цвет фона карточки (из текущей темы)
        )
    ) {
        // Row - располагает дочерние элементы в один горизонтальный ряд.
        Row(
            modifier = Modifier
                .fillMaxWidth() // Растягивает Row на всю ширину карточки
                .padding(16.dp), // Внутренние отступы для содержимого Row (16dp со всех сторон)
            horizontalArrangement = Arrangement.SpaceBetween, // Распределяет пространство между дочерними элементами,
            // первый элемент прижимается к началу, последний - к концу.
            verticalAlignment = Alignment.CenterVertically    // Выравнивает дочерние элементы по центру по вертикали.
        ) {
            // Левая колонка для времени отправления и названия начальной остановки
            Column(
                modifier = Modifier.weight(1f), // Занимает доступное пространство (с учетом других элементов с weight)
                horizontalAlignment = Alignment.Start // Выравнивает текст в этой колонке по левому краю
            ) {
                // Отображение времени отправления
                Text(
                    text = schedule.departureTime, // Текст времени отправления из объекта schedule
                    fontSize = 18.sp, // Размер шрифта
                    fontWeight = FontWeight.Bold, // Жирное начертание
                    color = MaterialTheme.colorScheme.primary // Цвет текста (основной цвет темы)
                )
                // Отображение названия начальной остановки (захардкожено)
                Text(
                    text = "Совгород (Рынок)", // Название начальной остановки
                    fontSize = 12.sp, // Меньший размер шрифта для дополнительной информации
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Приглушенный цвет текста
                )
            }

            // Правая колонка для времени прибытия и названия конечной остановки
            Column(
                modifier = Modifier.weight(1f), // Занимает доступное пространство
                horizontalAlignment = Alignment.End // Выравнивает содержимое этой колонки по правому краю
            ) {
                // Внутренняя Column для выравнивания текста времени и названия остановки по левому краю
                // относительно друг друга, но сама эта группа будет справа.
                Column(
                    horizontalAlignment = Alignment.Start // Текст внутри этой группы будет начинаться слева
                ) {
                    // Отображение времени прибытия
                    Text(
                        text = schedule.arrivalTime, // Текст времени прибытия из объекта schedule
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    // Отображение названия конечной остановки (захардкожено)
                    Text(
                        text = "Яровое (МСЧ-128)", // Название конечной остановки
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Иконка "Избранное", отображается только если передан onFavoriteClick
            if (onFavoriteClick != null) {
                // IconButton - кнопка с иконкой
                IconButton(
                    onClick = onFavoriteClick, // Действие при нажатии на иконку
                    modifier = Modifier.padding(start = 8.dp) // Отступ слева от иконки
                ) {
                    // Icon - компонент для отображения векторной иконки
                    Icon(
                        // Выбор иконки в зависимости от состояния isFavorite
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        // Описание контента для доступности
                        contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                        // Цвет иконки в зависимости от состояния isFavorite
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}