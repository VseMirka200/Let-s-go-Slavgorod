package com.example.slavgorodbus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle // <-- Импортируем TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slavgorodbus.data.model.BusRoute
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusRouteCard(
    route: BusRoute,
    onRouteClick: (BusRoute) -> Unit,
    onFavoriteClick: (BusRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onRouteClick(route) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Первый Row: номер маршрута, название, описание и кнопка "избранное".
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левая часть: кружок с номером маршрута, название и описание.
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(route.color.toColorInt())),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = route.routeNumber,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(modifier = Modifier.padding(end = 8.dp)) {
                        Text(
                            text = route.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = route.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
                IconButton(onClick = { onFavoriteClick(route) }) {
                    Icon(
                        imageVector = if (route.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (route.isFavorite) "Удалить из избранного" else "Добавить в избранное",
                        tint = if (route.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Второй Row: ЦЕНЫ (слева) и НАПРАВЛЕНИЕ (справа)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Левая часть: Цены
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // ОПРЕДЕЛЯЕМ ОБЩИЙ СТИЛЬ ДЛЯ ЦЕН
                    val commonPriceStyle = TextStyle(
                        fontSize = 12.sp, // Или любой другой размер по вашему выбору
                        fontWeight = FontWeight.Medium, // Или другой FontWeight
                        color = MaterialTheme.colorScheme.primary // Или другой цвет
                    )

                    var pricesDisplayed = false
                    route.pricePrimary?.let { priceText ->
                        if (priceText.isNotBlank()) {
                            Text(
                                text = priceText,
                                style = commonPriceStyle // <--- ПРИМЕНЯЕМ ОБЩИЙ СТИЛЬ
                            )
                            pricesDisplayed = true
                        }
                    }
                    route.priceSecondary?.let { priceText ->
                        if (priceText.isNotBlank()) {
                            Text(
                                text = priceText,
                                style = commonPriceStyle // <--- ПРИМЕНЯЕМ ОБЩИЙ СТИЛЬ
                            )
                            pricesDisplayed = true
                        }
                    }
                    if (!pricesDisplayed) {
                        // Spacer(Modifier.height(1.dp)) // Если нужно занять место
                    }
                }

                // Правая часть: Направление
                if (route.directionDetails?.isNotBlank() == true) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                            contentDescription = "Направление",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = route.directionDetails,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Spacer(Modifier.width(0.dp))
                }
            }
        }
    }
}