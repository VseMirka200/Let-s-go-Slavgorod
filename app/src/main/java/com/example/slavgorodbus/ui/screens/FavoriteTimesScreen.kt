package com.example.slavgorodbus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.slavgorodbus.R
import com.example.slavgorodbus.data.model.BusSchedule
import com.example.slavgorodbus.data.model.FavoriteTime
import com.example.slavgorodbus.ui.components.ScheduleCard
import com.example.slavgorodbus.ui.viewmodel.BusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTimesScreen(
    viewModel: BusViewModel,
    modifier: Modifier
) {
    val favoriteTimes by viewModel.favoriteTimes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.favorite_times_screen_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (favoriteTimes.isEmpty()) {
            EmptyFavoritesState(modifier = modifier.padding(paddingValues))
        } else {
            FavoritesList(
                modifier = modifier.padding(paddingValues),
                favoriteTimes = favoriteTimes,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun EmptyFavoritesState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = stringResource(id = R.string.no_favorite_times_icon_desc),
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(id = R.string.no_favorite_times_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(id = R.string.favorites_not_working_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun FavoritesList(
    modifier: Modifier = Modifier,
    favoriteTimes: List<FavoriteTime>,
    viewModel: BusViewModel
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = favoriteTimes,
            key = { favoriteTime -> favoriteTime.id }
        ) { favoriteTime ->
            val schedule = BusSchedule(
                id = favoriteTime.id,
                routeId = favoriteTime.routeId,
                stopName = favoriteTime.stopName,
                departureTime = favoriteTime.departureTime,
                arrivalTime = favoriteTime.arrivalTime,
                dayOfWeek = 1
            )

            ScheduleCard(
                schedule = schedule,
                isFavorite = true,
                onFavoriteClick = {
                    viewModel.removeFavoriteTime(favoriteTime.id)
                }
            )
        }
    }
}