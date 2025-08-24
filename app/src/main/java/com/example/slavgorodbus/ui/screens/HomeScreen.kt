package com.example.slavgorodbus.ui.screens

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
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight // <--- ДОБАВЬ ЭТОТ ИМПОРТ, ЕСЛИ ЕГО НЕТ
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.slavgorodbus.R
import com.example.slavgorodbus.data.model.BusRoute
import com.example.slavgorodbus.ui.components.BusRouteCard
import com.example.slavgorodbus.ui.components.SearchBar
import com.example.slavgorodbus.ui.viewmodel.BusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRouteClick: (BusRoute) -> Unit,
    viewModel: BusViewModel,
    navController: NavHostController, // NavController сейчас здесь не используется для заголовка
    modifier: Modifier.Companion
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name_actual),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold) // <--- ИЗМЕНЕНИЕ ЗДЕСЬ
                    )
                },
                actions = {
                    // Твои actions, если они есть
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = { /* Действие при поиске, если нужно */ },
            )

            when {
                uiState.isLoading -> LoadingState()
                uiState.error != null -> ErrorState(errorMessage = uiState.error!!) // Используем !! безопасно, так как есть проверка uiState.error != null
                uiState.routes.isEmpty() -> EmptyState(searchQuery = searchQuery)
                else -> RoutesListState(
                    routes = uiState.routes,
                    onRouteClick = onRouteClick
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(errorMessage: String) {
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
                contentDescription = null, // Рассмотрите добавление описания для доступности, например, stringResource(R.string.error_icon_desc)
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = stringResource(id = R.string.error_loading_routes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = errorMessage.ifEmpty { stringResource(id = R.string.unknown_error) },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyState(searchQuery: String) {
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
                contentDescription = null, // Рассмотрите stringResource(R.string.empty_state_icon_desc)
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (searchQuery.isNotEmpty()) stringResource(id = R.string.routes_not_found)
                else stringResource(id = R.string.routes_unavailable),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (searchQuery.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.try_changing_search_query),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RoutesListState(
    routes: List<BusRoute>,
    onRouteClick: (BusRoute) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp) // Рассмотрите возможность сделать отступы единообразными с padding(16.dp) или как вам нужно
    ) {
        items(
            items = routes,
            key = { route -> route.id } // Хорошая практика для производительности LazyColumn
        ) { route ->
            BusRouteCard(
                route = route,
                onRouteClick = onRouteClick
                // modifier = Modifier.padding(horizontal = 16.dp) // Если хотите отступы для карточек, согласующиеся с padding(16.dp) Column
            )
        }
    }
}