package com.example.android_practice.presentation.ui.screens.filters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android_practice.domain.model.FilterSettings
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBackClick: () -> Unit,
    onApplyFilters: (FilterSettings) -> Unit,
    viewModel: FilterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильтры") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (!uiState.filterSettings.isDefault) {
                        IconButton(onClick = { viewModel.clearFilters() }) {
                            Icon(Icons.Default.Clear, contentDescription = "Очистить")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveFilters()
                    onApplyFilters(uiState.filterSettings)
                    onBackClick()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Применить")
            }
        }
    ) { paddingValues ->
        FilterContent(
            filterSettings = uiState.filterSettings,
            onFilterSettingsChange = viewModel::updateFilterSettings,
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun FilterContent(
    filterSettings: FilterSettings,
    onFilterSettingsChange: (FilterSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Настройки фильтров",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Жанр
        OutlinedTextField(
            value = filterSettings.genre,
            onValueChange = { genre ->
                onFilterSettingsChange(filterSettings.copy(genre = genre))
            },
            label = { Text("Жанр") },
            placeholder = { Text("Например: комедия, драма") },
            modifier = Modifier.fillMaxWidth()
        )

        Column {
            Text(
                text = "Минимальный рейтинг: ${"%.1f".format(filterSettings.minRating)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = filterSettings.minRating.toFloat(),
                onValueChange = { rating ->
                    onFilterSettingsChange(filterSettings.copy(minRating = rating.toDouble()))
                },
                valueRange = 0f..10f,
                steps = 19,
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = if (filterSettings.yearFrom > 0) filterSettings.yearFrom.toString() else "",
            onValueChange = { year ->
                val yearInt = year.toIntOrNull() ?: 0
                onFilterSettingsChange(filterSettings.copy(yearFrom = yearInt))
            },
            label = { Text("Год выпуска от") },
            placeholder = { Text("Например: 2020") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        if (!filterSettings.isDefault) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Активные фильтры:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (filterSettings.genre.isNotEmpty()) {
                        Text("• Жанр: ${filterSettings.genre}")
                    }
                    if (filterSettings.minRating > 0) {
                        Text("• Мин. рейтинг: ${"%.1f".format(filterSettings.minRating)}")
                    }
                    if (filterSettings.yearFrom > 0) {
                        Text("• Год от: ${filterSettings.yearFrom}")
                    }
                }
            }
        }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = false
        ) {
            Text("Используйте кнопку в правом нижнем углу")
        }
    }
}